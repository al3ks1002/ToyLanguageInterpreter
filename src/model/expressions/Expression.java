package model.expressions;

import utils.HeapTable;
import utils.SymbolTable;

import java.io.Serializable;

public interface Expression extends Serializable {
  int evaluate(SymbolTable symbolTable, HeapTable heapTable);
}
