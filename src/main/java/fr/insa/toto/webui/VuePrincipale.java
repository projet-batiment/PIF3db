/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.toto.webui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 *
 * @author francois
 */
@Route(value = "")
@PageTitle("Likes")
public class VuePrincipale extends VerticalLayout {

    public VuePrincipale() {
        this.add(new H2("Bienvenu dans Likes"));
        Button bGoList = new Button("--> liste des utilisateurs");
        bGoList.addSingleClickListener((t) -> {
            UI.getCurrent().navigate(UtilisateurListPanel.class);
        });
        Button bGoNew = new Button("--> nouvel utilisateur");
        bGoNew.addSingleClickListener((t) -> {
            UI.getCurrent().navigate(NewUserPanel.class);
        });
        this.add(bGoList,bGoNew);

    }

}
