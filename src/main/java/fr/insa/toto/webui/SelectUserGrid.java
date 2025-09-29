/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.webui;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import fr.insa.toto.model.Utilisateur;
import java.util.List;

/**
 *
 * @author francois
 */
public class SelectUserGrid extends Grid<Utilisateur> {
    
    public SelectUserGrid(List<Utilisateur> users) {
        this.addColumn(Utilisateur::getSurnom);
        this.setItems(users);
        this.setSelectionMode(Grid.SelectionMode.MULTI);
    }
        
}
