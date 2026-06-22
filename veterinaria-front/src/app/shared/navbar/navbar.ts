import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styles: `
    .navbar { display: flex; align-items: center; justify-content: space-between; height: 56px; padding: 0 16px; background: #2e7d32; color: white; }
    .navbar-brand { display: flex; align-items: center; gap: 8px; font-size: 18px; font-weight: 500; color: white; text-decoration: none; }
    .navbar-user { display: flex; align-items: center; gap: 12px; font-size: 14px; }
    .navbar-user button { background: none; border: 1px solid rgba(255,255,255,0.5); color: white; padding: 4px 12px; border-radius: 4px; cursor: pointer; font-size: 13px; }
  `
})
export class Navbar {
  auth = inject(AuthService);
}
