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

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import fr.insa.lpe7.webui.session.LoginEntete;
import fr.insa.lpe7.webui.session.LogoutEntete;
import fr.insa.lpe7.webui.session.SessionInfo;

/**
 *
 * @author francois
 */
public class MainLayout extends AppLayout{
    
    public MainLayout() {
        this.addToDrawer(new MainMenu());
        DrawerToggle toggle = new DrawerToggle();
        this.addToNavbar(toggle);
        if (SessionInfo.userConnected()) {
            this.addToNavbar(new LogoutEntete());
        } else {
            this.addToNavbar(new LoginEntete());
        }
    }
    
}
