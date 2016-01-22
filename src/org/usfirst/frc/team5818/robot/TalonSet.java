package org.usfirst.frc.team5818.robot;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.Talon;

/**
 * A talon set holds an arbitrary amount of talons that can be manipulated as a
 * whole set.
 */
public class TalonSet {

	private final Set<Talon> talons;

	/**
	 * Merging constructor: Creates a new TalonSet that controls all talons in
	 * {@code sets}.
	 * 
	 * @param sets
	 *            - Multiple TalonSets that this TalonSet will control
	 */
	public TalonSet(TalonSet... sets) {
		this.talons = Stream.of(sets).flatMap(ts -> ts.talons.stream()).collect(Collectors.toSet());
	}

	/**
	 * Creates a new TalonSet that controls all talons in {@code talons}
	 * 
	 * @param talons
	 *            - The talons this TalonSet will control
	 */
	public TalonSet(Collection<? extends Talon> talons) {
		this.talons = new HashSet<>(talons);
	}

	/**
	 * Creates a new TalonSet that controls all talons in {@code talons}
	 * 
	 * @param talons
	 *            - The talons this TalonSet will control
	 */
	public TalonSet(Talon... talons) {
		this.talons = Stream.of(talons).collect(Collectors.toSet());
	}

	/**
	 * Returns an immutable list of the talons controlled by this TalonSet.
	 * 
	 * @return
	 */
	public Set<Talon> getTalons() {
		// TODO optimize
		return Collections.unmodifiableSet(talons);
	}

	/**
	 * Calls {@link Talon#set(double)} on every talon in this TalonSet.
	 * 
	 * @param power
	 *            - The power to pass to the talons
	 */
	public void sendPowerToTalons(double power) {
		talons.forEach(t -> t.set(power));
	}

}
