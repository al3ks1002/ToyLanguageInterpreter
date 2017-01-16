package controller;

import model.InterpreterException;
import model.ProgramState;
import repository.ProgramStateRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class InterpreterController {
  private final ProgramStateRepository repository;
  private ExecutorService executorService;

  public InterpreterController(ProgramStateRepository repository) {
    this.repository = repository;
  }

  private void executeOneStepForAllPrograms() {
    List<ProgramState> programStates = repository.getProgramStates();

    List<Callable<ProgramState>>
        callList =
        programStates.stream().map(
            (ProgramState programState) -> (Callable<ProgramState>) (programState::executeOneStep))
            .collect(Collectors.toList());

    List<ProgramState> newProgramStates;
    try {
      newProgramStates = executorService.invokeAll(callList).stream().map(future -> {
        try {
          return future.get();
        } catch (InterruptedException | ExecutionException e) {
          throw new InterpreterException(e.getMessage(), e.getCause());
        }
      }).filter(Objects::nonNull).collect(Collectors.toList());
    } catch (InterruptedException e) {
      throw new InterpreterException("Could not invoke the callables!", e);
    }

    newProgramStates.forEach(repository::addProgramState);
  }

  public void executeAll() {
    executorService = Executors.newFixedThreadPool(8);
    List<ProgramState> programStates = repository.getProgramStates();
    programStates.forEach(repository::logProgramState);
    while (!programStates.isEmpty()) {
      executeOneStepForAllPrograms();
      programStates.forEach(repository::logProgramState);
      removeCompletedPrograms();
    }
  }

  private void removeCompletedPrograms() {
    List<ProgramState> programStates = repository.getProgramStates();
    programStates.removeIf(ProgramState::isCompleted);
  }
}
