package com.riglerr.domain.util;
import com.riglerr.domain.entities.Alert;
import com.riglerr.domain.entities.MessageContext;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.time.LocalTime;

public class AlertMessageParserTests {

    @Test
    void parsesTimeCorrectly() throws Exception {
        MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D -m \"@raiders Raid starting in 30 minutes.\"");
        Alert outputAlert = AlertMessageParser.parse(context);
        assertCorrectTime(outputAlert.getTime());
    }

    @Test
    void exceptionIfTimeIsNullOrEmpty() {
        final MessageContext context = createTestContext("!raidalert -t -i P7D -m \"@raiders Raid starting in 30 minutes.\"");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void exceptionThrownIfTimeOptionNotSpecified() {
        final MessageContext context = createTestContext("!raidalert 16:30:00 -i P7D -m \"@raiders Raid starting in 30 minutes.\"");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void exceptionThrownIfTimeIsNotFormattedCorrectly() {
        final MessageContext context = createTestContext("!raidalert 87::3H1:0 -i P7D -m \"@raiders Raid starting in 30 minutes.\"");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void throwsExceptionIfTimeHasMoreThanOneParameter() {
        final MessageContext context = createTestContext("!raidalert -t 16:30:00 18:45:15 -i P7D -m \"@raiders Raid starting in 30 minutes.\"");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void parsesIntervalCorrectly() throws Exception {
        MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D -m \"@raiders Raid starting in 30 minutes.\"");
        Alert outputAlert = AlertMessageParser.parse(context);
        assertCorrectInterval(outputAlert.getIntervalInSeconds());
    }

    @Test
    void allowsNullIntervalAsZero() throws Exception {
        MessageContext context = createTestContext("!raidalert -t 16:30:00 -m \"@raiders Raid starting in 30 minutes.\"");
        Alert outputAlert = AlertMessageParser.parse(context);
        assertCorrectEmptyInterval(outputAlert.getIntervalInSeconds());
    }

    @Test
    void allowsEmptyIntervalAsZero() throws Exception {
        MessageContext context = createTestContext("!raidalert -t 16:30:00 -i -m \"@raiders Raid starting in 30 minutes.\"");
        Alert outputAlert = AlertMessageParser.parse(context);
        assertCorrectEmptyInterval(outputAlert.getIntervalInSeconds());
    }

    @Test
    void throwsExceptionIfIntervalHasMoreThanZeroParameters() {
        final MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D PT10S -m \"@raiders Raid starting in 30 minutes.\"");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void throwsExceptionIfIntervalImproperlyFormatted() {
        final MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7Dsadf -m \"@raiders Raid starting in 30 minutes.\"");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void parsesSingleMessageArgument() throws Exception {
        MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D -m message");
        Alert outputAlert = AlertMessageParser.parse(context);
        assertEquals("message", outputAlert.getMessageText());
    }

    @Test
    void parsesMultiWordMessageArgument() throws Exception {
        MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D -m @raiders Raid starting in 30 minutes.");
        Alert outputAlert = AlertMessageParser.parse(context);
        assertEquals("@raiders Raid starting in 30 minutes.", outputAlert.getMessageText());
    }

    @Test
    void throwsExceptionIfMessageOmitted() {
        final MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void throwsExceptionIfMessageIsNullOrEmpty() {
        final MessageContext context = createTestContext("!raidalert -t 16:30:00 -i P7D -m");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    @Test
    void throwsExceptionIfMessageContainsNonValidOption() {
        final MessageContext context = createTestContext("!raidalert -t 16:30:00 -y -i P7D -m @raiders Raid starting in 30 minutes. -b -z -a");
        assertThrows(Exception.class, () -> AlertMessageParser.parse(context));
    }

    private void assertCorrectTime(LocalTime actualTime) {
        assertEquals(LocalTime.parse("16:30:00"), actualTime);
    }

    private void assertCorrectInterval(long actualIntervalInSeconds) {
        assertEquals(Duration.parse("P7D").getSeconds(), actualIntervalInSeconds);
    }

    private void assertCorrectEmptyInterval(long actualIntervalInSeconds) {
        assertEquals(0, actualIntervalInSeconds);
    }

    private MessageContext createTestContext(String text) {
        return new MessageContext(text, 1, 1);
    }
}
