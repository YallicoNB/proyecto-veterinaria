import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth';

export const roleGuard = (allowedRoles: string[]) => {
  return () => {
    const auth = inject(AuthService);
    const router = inject(Router);
    const rol = auth.getRol();
    if (rol && allowedRoles.includes(rol)) {
      return true;
    }
    return router.parseUrl('/dashboard');
  };
};
