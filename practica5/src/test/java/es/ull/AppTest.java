package es.ull;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void main_printsHelloWorld_withEmptyArgs() {
        App.main(new String[0]);
        String printed = outContent.toString();
        assertTrue(printed.contains("Hello World"), "Should print Hello World");
    }

    @Test
    public void main_printsHelloWorld_withNullArgs() {
        App.main((String[]) null);
        String printed = outContent.toString();
        assertTrue(printed.contains("Hello World"), "Should print Hello World even with null args");
    }
}

