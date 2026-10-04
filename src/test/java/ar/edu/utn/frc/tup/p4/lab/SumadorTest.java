package ar.edu.utn.frc.tup.p4.lab;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SumadorTest {
    @Test
    void sumaDosNumeros() {
        assertEquals(5, Sumador.sumar(2, 2));
    }
}
