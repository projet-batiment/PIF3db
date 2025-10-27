/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.lpe7.webui;

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import fr.insa.lpe7.webui.utilisateurs.CreationAdmin;
import fr.insa.lpe7.webui.utilisateurs.ListeUtilisateurs;

/**
 *
 * @author francois
 */
public class MainMenu extends SideNav{
    
    public MainMenu() {
        SideNavItem accueil = new SideNavItem("accueil",VuePrincipale.class);
        SideNavItem utilisateurs = new SideNavItem("utilisateurs");
        SideNavItem listeUtilisateurs = new SideNavItem("liste",ListeUtilisateurs.class);
        SideNavItem creationAdmin = new SideNavItem("creation(admin)",CreationAdmin.class);
        utilisateurs.addItem(listeUtilisateurs);
        utilisateurs.addItem(creationAdmin);
        this.addItem(accueil,utilisateurs);
    }
    
}
