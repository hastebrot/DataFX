package io.datafx.samples.app;

import io.datafx.provider.ListDataProvider;
import io.datafx.provider.ListDataProviderBuilder;

import javax.inject.Inject;

/**
 * This Action Task adds some basic data to the data model. This model will be injected by CDI
 */
public class LoadPersonsTask implements Runnable {

    /**
     * the basic data
     */
    Person[] persons = {
        new Person("Johan Vos", "Johan is CTO at LodgON, a Java Champion, a member of the BeJUG steering group, the Devoxx steering group and he is a JCP member."),
        new Person("Jonathan Giles", "Jonathan Giles is the JavaFX UI controls technical lead at Oracle, where he has been involved with JavaFX since 2009."),
        new Person("Hendrik Ebbers", "Hendrik Ebbers is Senior Java Architect at Materna GmbH in Dortmund, Germany.")};

    /**
     * The data model of the flow. The model will be injected by CDI
     */
    @Inject
    private DataModel model;

    /**
     * The run method will be called whenever the flow action will be called.
     */
    @Override
    public void run() {
        model.getPersons().clear();
        for(int i = 0; i < persons.length; i++) {
            model.getPersons().add(persons[i]);
        }
    }
}
