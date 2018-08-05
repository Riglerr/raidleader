package com.riglerr.data;

import com.riglerr.data.exceptions.DuplicateCommandException;
import com.riglerr.data.exceptions.InvalidCommandException;
import com.riglerr.domain.entities.MessageContext;
import com.riglerr.domain.interfaces.UseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class CommandProcessorTest {
    @Test
    void canAddCommand() {
        var commandStore = new CommandStore();
        var expectedUsecase = mock(UseCase.class);
        var prefix = "!";
        var moduleIdentifier = "testModule";
        var commandIdentifier = "!testCommand";

        commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);

        var key = String.format("%s %s %s", prefix, moduleIdentifier, commandIdentifier);
        var actualUsecase = commandStore.getCommands().get(key);
        assertEquals(actualUsecase, expectedUsecase);
    }

    @Test
    void shouldThrowExceptionIfPrefixIsNull() {
        final CommandStore commandStore = new CommandStore();
        String prefix = null;
        var moduleIdentifier = "testModule";
        var commandIdentifier = "!testCommand";
        var expectedUsecase = mock(UseCase.class);
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "Prefix is Null or Empty");
    }

    @Test
    void shouldThrowExceptionIfPrefixIsEmpty() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "";
        var moduleIdentifier = "testModule";
        var commandIdentifier = "!testCommand";
        var expectedUsecase = mock(UseCase.class);
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "Prefix is Null or Empty");
    }

    @Test
    void shouldThrowExceptionIfModuleIdentifierIsNull() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "!";
        String moduleIdentifier = null;
        String commandIdentifier = "!testCommand";
        var expectedUsecase = mock(UseCase.class);
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "ModuleIdentifier is Null or Empty");
    }

    @Test
    void shouldThrowExceptionIfModuleIdentifierIsEmpty() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "!";
        String moduleIdentifier = "";
        String commandIdentifier = "!testCommand";
        var expectedUsecase = mock(UseCase.class);
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "ModuleIdentifier is Null or Empty");
    }

    @Test
    void shouldThrowExceptionIfCommandIdentifierIsNull() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "!";
        String moduleIdentifier = "testModule";
        String commandIdentifier = null;
        var expectedUsecase = mock(UseCase.class);
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "CommandIdentifier is Null or Empty");
    }

    @Test
    void shouldThrowExceptionIfCommandIdentifierIsEmpty() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "!";
        String moduleIdentifier = "testModule";
        String commandIdentifier = "";
        var expectedUsecase = mock(UseCase.class);
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "CommandIdentifier is Null or Empty");
    }

    @Test
    void shouldThrowExceptionIfUsecaseIsNull() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "!";
        String moduleIdentifier = "testModule";
        String commandIdentifier = "testCommand";
        UseCase expectedUsecase = null;
        assertThrows(InvalidCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        }, "Usecase cannot be null");
    }

    @Test
    void shouldRejectDupliates() {
        final CommandStore commandStore = new CommandStore();
        String prefix = "!";
        String moduleIdentifier = "testModule";
        String commandIdentifier = "testCommand";
        UseCase expectedUsecase = mock(UseCase.class);

        commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        assertThrows(DuplicateCommandException.class, ()-> {
            commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);
        });
    }

    @Test
    void executesCorrectCommand() {
        var commandStore = new CommandStore();
        var expectedUsecase = mock(UseCase.class);
        var prefix = "!";
        var moduleIdentifier = "testModule";
        var commandIdentifier = "testCommand";
        var message = String.format("%s%s %s", prefix, moduleIdentifier, commandIdentifier);
        var context = new MessageContext(message);
        commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);

        commandStore.executeCommand(context);
        verify(expectedUsecase).execute(context);
    }

    @Test
    void doesNothingForNoMatchingCommands() {
        var commandStore = new CommandStore();
        var expectedUsecase = mock(UseCase.class);
        var prefix = "!";
        var moduleIdentifier = "testModule";
        var commandIdentifier = "testCommand";
        var message = String.format("%s%s %s", prefix, moduleIdentifier, commandIdentifier);
        var context = new MessageContext("!aDifferent COmmand");
        commandStore.addCommand(prefix, moduleIdentifier, commandIdentifier, expectedUsecase);

        commandStore.executeCommand(context);
        verifyNoMoreInteractions(expectedUsecase);
    }
}