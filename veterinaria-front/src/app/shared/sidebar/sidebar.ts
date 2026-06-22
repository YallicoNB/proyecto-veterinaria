import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styles: `
    .sidebar { width: 240px; background: white; border-right: 1px solid #e0e0e0; display: flex; flex-direction: column; padding: 8px 0; }
    .sidebar a { display: flex; align-items: center; gap: 12px; padding: 10px 20px; text-decoration: none; color: rgba(0,0,0,0.7); font-size: 14px; transition: 0.2s; }
    .sidebar a:hover { background: #e8f5e9; }
    .sidebar a.active-link { background: #e8f5e9; color: #2e7d32; font-weight: 500; }
    .sidebar-section { padding: 8px 20px 4px; font-size: 11px; text-transform: uppercase; color: rgba(0,0,0,0.4); letter-spacing: 1px; }
  `
})
export class Sidebar {
  auth = inject(AuthService);

  tieneRol(roles: string[]): boolean {
    const r = this.auth.getRol();
    return r !== null && roles.includes(r);
  }
}
