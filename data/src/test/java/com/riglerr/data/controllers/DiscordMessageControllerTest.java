package com.riglerr.data.controllers;

import com.riglerr.data.interfaces.CommandModule;
import com.riglerr.domain.entities.MessageContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscordMessageControllerTest {

    @Test
    void shouldAllowSingleModuleAsParameter() {
        List<CommandModule> expectedModules = createListOfMockedCommandModules(1);

        var messageController = new DiscordMessageController(expectedModules.get(0));
        List<CommandModule> actualModulesList = messageController.getModules();

        assertExpectedModules(expectedModules, actualModulesList);
    }

    @Test
    void shouldAllowMultipleModulesAsParameters() {
        List<CommandModule> expectedModules = createListOfMockedCommandModules(3);

        var messageController = new DiscordMessageController(expectedModules.get(0),
                expectedModules.get(1), expectedModules.get(2));
        var actualModulesList = messageController.getModules();

        assertExpectedModules(expectedModules, actualModulesList);
    }

    @Test
    void shouldRejectDuplicateModules() {
        var module = createMockCommandModule("!", "moduleIdentifier");

        try {
            new DiscordMessageController(module, module);
            fail("Did not throw exception.");
        } catch (RuntimeException e) {
            assertEquals("Duplicate CommandModule added.", e.getMessage());
        }
    }

    @Test
    void shouldRejectModulesWithEmptyPrefix() {
        var module = createMockCommandModule("", "moduleIdentifier");

        try {
            new DiscordMessageController(module);
            fail("Did not throw exception for Empty prefix");
        } catch (Exception e) {
            assertEquals("Module prefix is null or empty.", e.getMessage());
        }
    }

    @Test
    void shouldRejectModulesWithNullPrefix() {
        var module = createMockCommandModule(null, "moduleIdentifier");

        try {
            new DiscordMessageController(module);
            fail("Did not throw exception for Empty prefix");
        } catch (Exception e) {
            assertEquals("Module prefix is null or empty.", e.getMessage());
        }
    }

    @Test
    void shouldRejectModulesWithEmptyIdentifier() {
        var module = createMockCommandModule("!", "");

        try {
            new DiscordMessageController(module);
            fail("Did not throw exception for Empty prefix");
        } catch (Exception e) {
            assertEquals("Module identifier is null or empty.", e.getMessage());
        }
    }

    @Test
    void shouldRejectModulesWithNullIdentifier() {
        var module = createMockCommandModule("!", null);

        try {
            new DiscordMessageController(module);
            fail("Did not throw exception for Empty prefix");
        } catch (Exception e) {
            assertEquals("Module identifier is null or empty.", e.getMessage());
        }
    }

    @Test
    void shouldExecuteMatchingModule() {
        var module = createMockCommandModule("!", "myModule");
        var context = mock(MessageContext.class);
        when(context.getText()).thenReturn("!myModule test");

        var controller = new DiscordMessageController(module);
        controller.processMessage(context);

        verify(module, times(1)).execute(context);
    }

    @Test
    void shouldNotExecuteIfNoMatchingModules() {
        var module = createMockCommandModule("!", "myModule");
        var context = mock(MessageContext.class);
        when(context.getText()).thenReturn("!myMuddle test");

        var controller = new DiscordMessageController(module);
        controller.processMessage(context);

        verify(module, never()).execute(any(MessageContext.class));
    }

    private List<CommandModule> createListOfMockedCommandModules(int numModules) {
        List<CommandModule> moduleList = new ArrayList<>();
        for (var i = 0; i < numModules; i++) {
            var module = createMockCommandModule("!", String.format("module%s", i +1));
            moduleList.add(module);
        }
        return moduleList;
    }

    private CommandModule createMockCommandModule(String prefix, String identifier) {
        var module = mock(CommandModule.class);
        when(module.getModulePrefix()).thenReturn(prefix);
        when(module.getModuleIdentifier()).thenReturn(identifier);
        return module;
    }

    private void assertExpectedModules(List<CommandModule> expectedModules, List<CommandModule> actualModules) {
        assertEquals(expectedModules.size(), actualModules.size());
        for (CommandModule module : expectedModules) {
            assertEquals(true, actualModules.contains(module));
        }
    }

}