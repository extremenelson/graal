/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.graal.phases.tiers;

import com.oracle.graal.lir.phases.LIRSuites;
import com.oracle.graal.options.OptionValues;
import com.oracle.graal.phases.PhaseSuite;

public final class Suites {

    private final PhaseSuite<HighTierContext> highTier;
    private final PhaseSuite<MidTierContext> midTier;
    private final PhaseSuite<LowTierContext> lowTier;
    private boolean immutable;

    public PhaseSuite<HighTierContext> getHighTier() {
        return highTier;
    }

    public PhaseSuite<MidTierContext> getMidTier() {
        return midTier;
    }

    public PhaseSuite<LowTierContext> getLowTier() {
        return lowTier;
    }

    public Suites(PhaseSuite<HighTierContext> highTier, PhaseSuite<MidTierContext> midTier, PhaseSuite<LowTierContext> lowTier) {
        this.highTier = highTier;
        this.midTier = midTier;
        this.lowTier = lowTier;
    }

    public static Suites createSuites(CompilerConfiguration config, OptionValues options) {
        return new Suites(config.createHighTier(options), config.createMidTier(options), config.createLowTier(options));
    }

    public static LIRSuites createLIRSuites(CompilerConfiguration config, OptionValues options) {
        return new LIRSuites(config.createPreAllocationOptimizationStage(options), config.createAllocationStage(options), config.createPostAllocationOptimizationStage(options));
    }

    public boolean isImmutable() {
        return immutable;
    }

    public synchronized void setImmutable() {
        if (!immutable) {
            highTier.setImmutable();
            midTier.setImmutable();
            lowTier.setImmutable();
            immutable = true;
        }
    }

    public Suites copy() {
        return new Suites(highTier.copy(), midTier.copy(), lowTier.copy());
    }
}
