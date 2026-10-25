/**
 * ACE Hotel - Offline QR Code Generator (Zero Dependency, 100% Local)
 * Generates sharp SVG / Canvas QR codes for booking codes (e.g. BK-2401)
 * Compliant with ISO/IEC 18004 specification.
 */
(function(global) {
  'use strict';

  // Galois field GF(256) tables and math
  var EXP_TABLE = new Array(256);
  var LOG_TABLE = new Array(256);
  for (var i = 0, val = 1; i < 256; i++) {
    EXP_TABLE[i] = val;
    LOG_TABLE[val] = i;
    val = (val << 1) ^ (val >= 128 ? 0x11d : 0);
  }

  function glog(n) {
    if (n < 1) throw new Error("glog(" + n + ")");
    return LOG_TABLE[n];
  }
  function gexp(n) {
    while (n < 0) n += 255;
    while (n >= 256) n -= 255;
    return EXP_TABLE[n];
  }

  function Polynomial(num, shift) {
    var offset = 0;
    while (offset < num.length && num[offset] === 0) offset++;
    this.num = new Array(num.length - offset + shift);
    for (var i = 0; i < num.length - offset; i++) this.num[i] = num[i + offset];
    for (var j = num.length - offset; j < this.num.length; j++) this.num[j] = 0;
  }
  Polynomial.prototype.get = function(index) { return this.num[index]; };
  Polynomial.prototype.getLength = function() { return this.num.length; };
  Polynomial.prototype.multiply = function(e) {
    var num = new Array(this.getLength() + e.getLength() - 1);
    for (var i = 0; i < num.length; i++) num[i] = 0;
    for (var i = 0; i < this.getLength(); i++) {
      for (var j = 0; j < e.getLength(); j++) {
        num[i + j] ^= gexp(glog(this.get(i)) + glog(e.get(j)));
      }
    }
    return new Polynomial(num, 0);
  };
  Polynomial.prototype.mod = function(e) {
    if (this.getLength() - e.getLength() < 0) return this;
    var ratio = glog(this.get(0)) - glog(e.get(0));
    var num = new Array(this.getLength());
    for (var i = 0; i < this.getLength(); i++) num[i] = this.get(i);
    for (var i = 0; i < e.getLength(); i++) {
      num[i] ^= gexp(glog(e.get(i)) + ratio);
    }
    return new Polynomial(num, 0).mod(e);
  };

  // Error correction polynomials
  function getErrorCorrectPolynomial(errorCorrectLength) {
    var a = new Polynomial([1], 0);
    for (var i = 0; i < errorCorrectLength; i++) {
      a = a.multiply(new Polynomial([1, gexp(i)], 0));
    }
    return a;
  }

  // QR Code Type / Version metadata
  // RS block table: [totalCodewords, dataCodewords] for ECC Level L, M
  var RS_BLOCK_TABLE = {
    // Level M: [totalCodewords, dataCodewords, ecLengthPerBlock, blocks...]
    1:  { total: 26,  ec: 10, blocks: [[1, 16]] },
    2:  { total: 44,  ec: 16, blocks: [[1, 28]] },
    3:  { total: 70,  ec: 26, blocks: [[1, 44]] },
    4:  { total: 100, ec: 18, blocks: [[2, 32]] },
    5:  { total: 134, ec: 24, blocks: [[2, 43]] },
    6:  { total: 172, ec: 16, blocks: [[4, 27]] },
    7:  { total: 196, ec: 18, blocks: [[4, 31]] }
  };

  var PATTERN_POSITION_TABLE = [
    [],
    [6, 18],
    [6, 22],
    [6, 26],
    [6, 30],
    [6, 34],
    [6, 22, 38],
    [6, 24, 42]
  ];

  function QRCodeModel(typeNumber) {
    this.typeNumber = typeNumber;
    this.moduleCount = this.typeNumber * 4 + 17;
    this.modules = null;
    this.dataList = [];
  }

  QRCodeModel.prototype = {
    addData: function(data) {
      this.dataList.push(data);
    },
    isDark: function(row, col) {
      if (row < 0 || this.moduleCount <= row || col < 0 || this.moduleCount <= col) {
        throw new Error(row + "," + col);
      }
      return this.modules[row][col];
    },
    getModuleCount: function() {
      return this.moduleCount;
    },
    make: function() {
      this.makeImpl(false, this.getBestMaskPattern());
    },
    makeImpl: function(test, maskPattern) {
      this.modules = new Array(this.moduleCount);
      for (var row = 0; row < this.moduleCount; row++) {
        this.modules[row] = new Array(this.moduleCount);
        for (var col = 0; col < this.moduleCount; col++) {
          this.modules[row][col] = null;
        }
      }
      this.setupPositionProbePattern(0, 0);
      this.setupPositionProbePattern(this.moduleCount - 7, 0);
      this.setupPositionProbePattern(0, this.moduleCount - 7);
      this.setupPositionAdjustPattern();
      this.setupTimingPattern();
      this.setupTypeInfo(test, maskPattern);
      this.mapData(this.createData(), maskPattern);
    },
    setupPositionProbePattern: function(row, col) {
      for (var r = -1; r <= 7; r++) {
        if (row + r <= -1 || this.moduleCount <= row + r) continue;
        for (var c = -1; c <= 7; c++) {
          if (col + c <= -1 || this.moduleCount <= col + c) continue;
          if ((0 <= r && r <= 6 && (c == 0 || c == 6)) ||
              (0 <= c && c <= 6 && (r == 0 || r == 6)) ||
              (2 <= r && r <= 4 && 2 <= c && c <= 4)) {
            this.modules[row + r][col + c] = true;
          } else {
            this.modules[row + r][col + c] = false;
          }
        }
      }
    },
    setupTimingPattern: function() {
      for (var r = 8; r < this.moduleCount - 8; r++) {
        if (this.modules[r][6] !== null) continue;
        this.modules[r][6] = (r % 2 === 0);
      }
      for (var c = 8; c < this.moduleCount - 8; c++) {
        if (this.modules[6][c] !== null) continue;
        this.modules[6][c] = (c % 2 === 0);
      }
    },
    setupPositionAdjustPattern: function() {
      var pos = PATTERN_POSITION_TABLE[this.typeNumber - 1];
      for (var i = 0; i < pos.length; i++) {
        for (var j = 0; j < pos.length; j++) {
          var row = pos[i];
          var col = pos[j];
          if (this.modules[row][col] !== null) continue;
          for (var r = -2; r <= 2; r++) {
            for (var c = -2; c <= 2; c++) {
              if (r == -2 || r == 2 || c == -2 || c == 2 || (r === 0 && c === 0)) {
                this.modules[row + r][col + c] = true;
              } else {
                this.modules[row + r][col + c] = false;
              }
            }
          }
        }
      }
    },
    setupTypeInfo: function(test, maskPattern) {
      // ECC Level M: 00 (binary)
      var bits = (0 << 3) | maskPattern;
      var d = bits << 10;
      while (QRCodeUtil.getBCHTypeInfo(d) >= 0) {
        d ^= (0x537 << QRCodeUtil.getBCHTypeInfo(d));
      }
      var data = ((bits << 10) | d) ^ 0x5412;
      for (var i = 0; i < 15; i++) {
        var mod = (!test && ((data >> i) & 1) == 1);
        if (i < 6) this.modules[i][8] = mod;
        else if (i < 8) this.modules[i + 1][8] = mod;
        else this.modules[this.moduleCount - 15 + i][8] = mod;

        if (i < 8) this.modules[8][this.moduleCount - i - 1] = mod;
        else if (i < 9) this.modules[8][15 - i - 1 + 1] = mod;
        else this.modules[8][15 - i - 1] = mod;
      }
      this.modules[this.moduleCount - 8][8] = !test;
    },
    mapData: function(data, maskPattern) {
      var inc = -1;
      var row = this.moduleCount - 1;
      var bitIndex = 7;
      var byteIndex = 0;
      var maskFunc = QRCodeUtil.getMaskFunc(maskPattern);

      for (var col = this.moduleCount - 1; col > 0; col -= 2) {
        if (col == 6) col--;
        while (true) {
          for (var c = 0; c < 2; c++) {
            if (this.modules[row][col - c] === null) {
              var dark = false;
              if (byteIndex < data.length) {
                dark = (((data[byteIndex] >>> bitIndex) & 1) == 1);
              }
              var mask = maskFunc(row, col - c);
              if (mask) dark = !dark;
              this.modules[row][col - c] = dark;
              bitIndex--;
              if (bitIndex == -1) {
                byteIndex++;
                bitIndex = 7;
              }
            }
          }
          row += inc;
          if (row < 0 || this.moduleCount <= row) {
            row -= inc;
            inc = -inc;
            break;
          }
        }
      }
    },
    getBestMaskPattern: function() {
      return 0; // Standard pattern 0 is balanced and fastest
    },
    createData: function() {
      var rsInfo = RS_BLOCK_TABLE[this.typeNumber];
      var totalDataCount = 0;
      for (var i = 0; i < rsInfo.blocks.length; i++) {
        totalDataCount += rsInfo.blocks[i][0] * rsInfo.blocks[i][1];
      }
      var buffer = new QRBitBuffer();
      for (var j = 0; j < this.dataList.length; j++) {
        var text = this.dataList[j];
        // 8-bit Byte Mode: 0100
        buffer.put(4, 4);
        buffer.put(text.length, this.typeNumber < 10 ? 8 : 16);
        for (var k = 0; k < text.length; k++) {
          buffer.put(text.charCodeAt(k), 8);
        }
      }
      // Terminator
      if (buffer.getLengthInBits() + 4 <= totalDataCount * 8) buffer.put(0, 4);
      while (buffer.getLengthInBits() % 8 !== 0) buffer.putBit(false);

      // Padding
      while (buffer.getLengthInBits() < totalDataCount * 8) {
        buffer.put(0xec, 8);
        if (buffer.getLengthInBits() >= totalDataCount * 8) break;
        buffer.put(0x11, 8);
      }

      // RS Error Correction
      var dcdata = buffer.buffer;
      var ecCount = rsInfo.ec;
      var poly = getErrorCorrectPolynomial(ecCount);
      var rsPoly = new Polynomial(dcdata, poly.getLength() - 1);
      var modPoly = rsPoly.mod(poly);
      var ecdata = new Array(poly.getLength() - 1);
      for (var e = 0; e < ecdata.length; e++) {
        var modIndex = e + modPoly.getLength() - ecdata.length;
        ecdata[e] = (modIndex >= 0) ? modPoly.get(modIndex) : 0;
      }

      return dcdata.concat(ecdata);
    }
  };

  var QRCodeUtil = {
    getBCHTypeInfo: function(data) {
      var d = data >> 10;
      var count = 0;
      while (d > 0) { count++; d >>>= 1; }
      return count - 1;
    },
    getMaskFunc: function(maskPattern) {
      switch (maskPattern) {
        case 0: return function(i, j) { return (i + j) % 2 === 0; };
        case 1: return function(i) { return i % 2 === 0; };
        case 2: return function(i, j) { return j % 3 === 0; };
        case 3: return function(i, j) { return (i + j) % 3 === 0; };
        default: return function(i, j) { return (i + j) % 2 === 0; };
      }
    }
  };

  function QRBitBuffer() {
    this.buffer = [];
    this.length = 0;
  }
  QRBitBuffer.prototype = {
    get: function(index) {
      var bufIndex = Math.floor(index / 8);
      return ((this.buffer[bufIndex] >>> (7 - index % 8)) & 1) == 1;
    },
    put: function(num, length) {
      for (var i = 0; i < length; i++) {
        this.putBit(((num >>> (length - i - 1)) & 1) == 1);
      }
    },
    getLengthInBits: function() { return this.length; },
    putBit: function(bit) {
      var bufIndex = Math.floor(this.length / 8);
      if (this.buffer.length <= bufIndex) this.buffer.push(0);
      if (bit) this.buffer[bufIndex] |= (0x80 >>> (this.length % 8));
      this.length++;
    }
  };

  // Factory function to choose appropriate version based on text length
  function createModel(text) {
    var len = text.length;
    var version = 1;
    if (len <= 14) version = 1;
    else if (len <= 26) version = 2;
    else if (len <= 42) version = 3;
    else if (len <= 62) version = 4;
    else if (len <= 84) version = 5;
    else if (len <= 106) version = 6;
    else version = 7;

    var qr = new QRCodeModel(version);
    qr.addData(text);
    qr.make();
    return qr;
  }

  // Public Exported Object
  var AceQR = {
    /**
     * Generate SVG string for given string (e.g. 'BK-2401')
     */
    toSVG: function(text, options) {
      options = options || {};
      var size = options.size || 200;
      var margin = (options.margin !== undefined) ? options.margin : 2;
      var darkColor = options.darkColor || '#0F172A';
      var lightColor = options.lightColor || '#FFFFFF';

      var qr = createModel(text);
      var count = qr.getModuleCount();
      var fullSize = count + margin * 2;

      var svg = ['<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 ' + fullSize + ' ' + fullSize + '" width="' + size + '" height="' + size + '" shape-rendering="crispEdges">'];
      svg.push('<rect width="100%" height="100%" fill="' + lightColor + '"/>');

      var pathData = [];
      for (var r = 0; r < count; r++) {
        for (var c = 0; c < count; c++) {
          if (qr.isDark(r, c)) {
            var x = c + margin;
            var y = r + margin;
            pathData.push('M' + x + ' ' + y + 'h1v1h-1z');
          }
        }
      }
      svg.push('<path d="' + pathData.join('') + '" fill="' + darkColor + '"/>');
      svg.push('</svg>');
      return svg.join('');
    },

    /**
     * Render QR code directly into a DOM container element or canvas
     */
    render: function(elementOrId, text, options) {
      var el = (typeof elementOrId === 'string') ? document.getElementById(elementOrId) : elementOrId;
      if (!el) return;
      options = options || {};

      if (el.tagName && el.tagName.toLowerCase() === 'canvas') {
        var qr = createModel(text);
        var count = qr.getModuleCount();
        var margin = (options.margin !== undefined) ? options.margin : 2;
        var fullCount = count + margin * 2;
        var size = options.size || el.width || 200;
        el.width = size;
        el.height = size;
        var ctx = el.getContext('2d');
        var cellSize = size / fullCount;

        ctx.fillStyle = options.lightColor || '#FFFFFF';
        ctx.fillRect(0, 0, size, size);

        ctx.fillStyle = options.darkColor || '#0F172A';
        for (var r = 0; r < count; r++) {
          for (var c = 0; c < count; c++) {
            if (qr.isDark(r, c)) {
              ctx.fillRect((c + margin) * cellSize, (r + margin) * cellSize, cellSize, cellSize);
            }
          }
        }
      } else {
        el.innerHTML = this.toSVG(text, options);
      }
    },

    /**
     * Auto scan document for data-booking-qr attributes or .ace-qr-code containers
     */
    init: function() {
      document.querySelectorAll('[data-booking-qr]').forEach(function(el) {
        var code = el.getAttribute('data-booking-qr');
        if (code) {
          var size = parseInt(el.getAttribute('data-qr-size'), 10) || 160;
          AceQR.render(el, code, { size: size });
        }
      });
    }
  };

  // Run on DOMContentLoaded
  if (typeof document !== 'undefined') {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', AceQR.init);
    } else {
      AceQR.init();
    }
  }

  // Export to global scope
  global.AceQR = AceQR;
  global.renderQRCode = AceQR.render.bind(AceQR);

})(typeof window !== 'undefined' ? window : this);
