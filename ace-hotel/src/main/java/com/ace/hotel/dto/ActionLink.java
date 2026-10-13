package com.ace.hotel.dto;

/** Một nút thao tác trên thẻ phòng hoặc dòng bảng. */
public record ActionLink(String label, String href, boolean primary) {
}
