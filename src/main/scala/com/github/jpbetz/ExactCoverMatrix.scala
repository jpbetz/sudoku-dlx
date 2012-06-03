package com.github.jpbetz

trait ExactCoverMatrix {
  def isEmpty : Boolean;
  def columns() : Iterator[Column];
}

trait Column {
  def next() : Column;
  def previous() : Column;
  def size() : Integer;
  def nodes() : Iterator[Node];
  def coverColumn();
  def uncoverColumn();
}

trait Node {
  def getRowId() : Integer;
  def allNodesInRow() : Iterator[Node];
  def getColumn() : Column;
}