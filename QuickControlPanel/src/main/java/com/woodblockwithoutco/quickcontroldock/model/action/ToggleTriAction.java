/*******************************************************************************
 * Copyright 2014 Alexander Leontyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.woodblockwithoutco.quickcontroldock.model.action;

import java.util.EnumSet;

public abstract class ToggleTriAction {
	
	public static enum State {
		FIRST,
		SECOND,
		THIRD
	}
	
	private EnumSet<State> mAvailableActions = EnumSet.of(State.FIRST, State.SECOND, State.THIRD);

	public final void performAction() {
		switch(getCurrentState()) {
		case FIRST:
			if(mAvailableActions.contains(State.SECOND)) {
				performSecondAction();
			} else if(mAvailableActions.contains(State.THIRD)) {
				performThirdAction();
			}
			break;
		case SECOND:
			if(mAvailableActions.contains(State.THIRD)) {
				performThirdAction();
			} else if(mAvailableActions.contains(State.FIRST)) {
				performFirstAction();
			}
			break;
		case THIRD:
			if(mAvailableActions.contains(State.FIRST)) {
				performFirstAction();
			} else if(mAvailableActions.contains(State.SECOND)) {
				performSecondAction();
			}
			break;
		}
	}
	
	public final void excludeActionForState(State action) {
		mAvailableActions.remove(action);
	}
	
	public final void includeActionForState(State action) {
		mAvailableActions.add(action);
	}
	
	public abstract State getCurrentState();
	protected abstract void performFirstAction();
	protected abstract void performSecondAction();
	protected abstract void performThirdAction();
	
	
	/**
	 * Override this method for actions which can be in indefinite state
	 * @return false.
	 */
	public boolean isStateIndefinite() {
		return false;
	}
}
