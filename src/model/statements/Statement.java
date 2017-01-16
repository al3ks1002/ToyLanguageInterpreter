package model.statements;

import model.ProgramState;

import java.io.Serializable;

public interface Statement extends Serializable {
  ProgramState execute(ProgramState programState);
}
