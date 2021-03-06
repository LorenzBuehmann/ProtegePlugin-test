/**
 * Copyright (C) 2007-2009, Jens Lehmann
 *
 * This file is part of DL-Learner.
 * 
 * DL-Learner is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * DL-Learner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.dllearner.tools.protege;

import java.util.Collections;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerStatus;
import org.protege.editor.owl.ui.editor.AbstractOWLClassExpressionEditor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * This is the class that must be implemented to get the plugin integrated in
 * protege.
 * 
 * @author Christian Koetteritzsch
 * 
 */
public class DLLearnerProtegePlugin extends AbstractOWLClassExpressionEditor implements OWLModelManagerListener{
	private JComponent view;
	
	@Override
	public JComponent getComponent() {
		view = new JPanel();
		view.add(new JLabel("DL-Learner Plugin"));
		return view;
	}

	@Override
	public Set<OWLClassExpression> getClassExpressions() {
		return Collections.emptySet();
	}

	@Override
	public boolean isValidInput() {
		checkReasonerStatus();
		return true;
	}
	
	private void checkReasonerStatus() {
		OWLReasonerManager reasonerManager = getOWLEditorKit().getOWLModelManager().getOWLReasonerManager();
		ReasonerStatus status = reasonerManager.getReasonerStatus();
		System.out.println(status);
	}

	@Override
	public boolean setDescription(OWLClassExpression arg0) {
		return true;
	}

	@Override
	public void initialise() throws Exception {
		System.out.println("Initializing DL-Learner plugin...");
		addListeners();
	}

	@Override
	public void dispose() throws Exception {
		removeListeners();
	}

	@Override
	public void addStatusChangedListener(
			InputVerificationStatusChangedListener arg0) {
	}

	@Override
	public void removeStatusChangedListener(
			InputVerificationStatusChangedListener arg0) {
	}
	
	private void addListeners(){
		getOWLEditorKit().getOWLModelManager().addListener(this);
	}
	
	private void removeListeners(){
		getOWLEditorKit().getOWLModelManager().removeListener(this);
	}

	@Override
	public void handleChange(OWLModelManagerChangeEvent event) {
		OWLClass lastSelectedClass = getOWLEditorKit().getOWLWorkspace().getOWLSelectionModel().getLastSelectedClass();
        if (lastSelectedClass != null) {
			if(event.isType(EventType.REASONER_CHANGED) && !event.isType(EventType.ACTIVE_ONTOLOGY_CHANGED)){
				checkReasonerStatus();
			}
        }
	}
}
