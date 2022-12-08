package com.baitapnhom.entity;
public class Card {
private Long id;
private int sl;
public Long getId() {
    return id;
}
public void setId(Long id) {
    this.id = id;
}
public int getSl() {
    return sl;
}
public void setSl(int sl) {
    this.sl = sl;
}
public Card(Long id, int sl) {
    super();
    this.id = id;
    this.sl = sl;
}
public Card() {
    super();
    // TODO Auto-generated constructor stub
}

}
