package repository;

import model.ProgramState;

import java.io.Serializable;
import java.util.List;

public interface ProgramStateRepository extends Serializable {
  void addProgramState(ProgramState programState);

  List<ProgramState> getProgramStates();

  ProgramState getProgramStateWithId(int index);

  void logProgramState(ProgramState programState);

  void garbageCollectProgramState(ProgramState programState);

  void serializeProgramState(ProgramState programState, String fileName);

  void deserializeProgramState(String fileName);
}
