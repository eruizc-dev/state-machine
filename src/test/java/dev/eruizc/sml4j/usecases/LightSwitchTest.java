package dev.eruizc.sml4j.usecases;

import static org.junit.jupiter.api.Assertions.*;

import dev.eruizc.sml4j.*;
import org.junit.jupiter.api.*;

/*
 * Simple state machine with only two states
 *
 * Starts off
 * When off: can be turned on
 * When on: can be turned off
 * */
public class LightSwitchTest {
	StateMachine<Status, Actions> lightSwitch;

	@BeforeEach
	void beforeEach() {
		lightSwitch = new StateMachineBuilder<Status, Actions>(Status.OFF)
				.allowTransition(Status.OFF, Actions.TURN_ON, Status.ON)
				.allowTransition(Status.ON, Actions.TURN_OFF, Status.OFF)
				.build();
	}

	@Test
	void startsOff() {
		assertEquals(Status.OFF, lightSwitch.getState());
	}

	@Test
	void turnsOn() throws Exception {
		lightSwitch.transition(Actions.TURN_ON);
		assertEquals(Status.ON, lightSwitch.getState());
	}

	@Test
	void turnsOnAndOff() throws Exception {
		lightSwitch.transition(Actions.TURN_ON);
		lightSwitch.transition(Actions.TURN_OFF);
		assertEquals(Status.OFF, lightSwitch.getState());
	}

	@Test
	void cannotTurnOff() {
		try {
			lightSwitch.transition(Actions.TURN_OFF);
			assert false: "Expected transition to throw";
		} catch (IllegalTransitionException ex) {
			assertEquals(Status.OFF, ex.getState());
			assertEquals(Actions.TURN_OFF, ex.getAction());
		}
	}

	@Test
	void cannotTurnOnAndoffTwice() throws Exception {
		lightSwitch.transition(Actions.TURN_ON);
		lightSwitch.transition(Actions.TURN_OFF);
		try {
			lightSwitch.transition(Actions.TURN_OFF);
			assert false: "Expected transition to throw";
		} catch (IllegalTransitionException ex) {
			assertEquals(Status.OFF, ex.getState());
			assertEquals(Actions.TURN_OFF, ex.getAction());
		}
	}

	static enum Status {
		ON, OFF;
	}

	static enum Actions {
		TURN_ON, TURN_OFF;
	}
}
