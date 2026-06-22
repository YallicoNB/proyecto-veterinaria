import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styles: `
    .navbar { position: fixed; top: 0; left: 0; right: 0; z-index: 1000; display: flex; align-items: center; height: 56px; padding: 0 16px; background: #2e7d32; color: white; }
    .navbar-brand { display: flex; align-items: center; gap: 8px; font-size: 18px; font-weight: 500; color: white; text-decoration: none; margin-right: 24px; }
    .navbar-links { display: flex; align-items: center; gap: 4px; flex: 1; }
    .navbar-links a { display: flex; align-items: center; gap: 6px; padding: 6px 12px; color: rgba(255,255,255,0.8); text-decoration: none; font-size: 14px; border-radius: 4px; transition: 0.2s; }
    .navbar-links a:hover { background: rgba(255,255,255,0.15); color: white; }
    .navbar-links a.active-link { background: rgba(255,255,255,0.2); color: white; font-weight: 500; }
    .navbar-user { display: flex; align-items: center; gap: 12px; font-size: 14px; margin-left: auto; flex-shrink: 0; }
    .navbar-user .username { max-width: 140px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .navbar-user .role-badge { font-size: 11px; padding: 2px 8px; border-radius: 10px; background: rgba(255,255,255,0.2); text-transform: uppercase; }
    .navbar-user button { background: none; border: 1px solid rgba(255,255,255,0.5); color: white; padding: 4px 12px; border-radius: 4px; cursor: pointer; font-size: 13px; transition: 0.2s; }
    .navbar-user button:hover { background: rgba(255,255,255,0.15); }
    .menu-toggle { display: none; background: none; border: none; color: white; font-size: 24px; cursor: pointer; padding: 4px; margin-left: auto; }
    #menu-checkbox { display: none; }

    @media (max-width: 768px) {
      .menu-toggle { display: block; }
      .navbar-links { display: none; position: absolute; top: 56px; left: 0; right: 0; background: #2e7d32; flex-direction: column; padding: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.2); }
      .navbar-links a { padding: 10px 12px; width: 100%; }
      #menu-checkbox:checked + .menu-toggle + .navbar-links { display: flex; }
      .navbar-user .username { display: none; }
      .navbar-user .role-badge { display: none; }
    }
  `
})
export class Navbar {
  auth = inject(AuthService);
  menuOpen = false;

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }
}
