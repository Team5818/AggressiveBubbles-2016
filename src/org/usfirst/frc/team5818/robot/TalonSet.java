package org.usfirst.frc.team5818.robot;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.Talon;

public class TalonSet {

	private Set<Talon> talons;

	public TalonSet(TalonSet... sets) {
		this.talons = Stream.of(sets).flatMap(ts -> ts.talons.stream()).collect(Collectors.toSet());
	}

	public TalonSet(Collection<? extends Talon> talons) {
		this.talons = new HashSet<>(talons);
	}

	public TalonSet(Talon... talons) {
		this.talons = Stream.of(talons).collect(Collectors.toSet());
	}

	public Set<Talon> getTalons() {
		return talons;
	}

	public void sendPowerToTalons(double power) {
		talons.forEach(t -> t.set(power));
	}

}
