import { Component, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styles: [`
    .login-container { display: flex; justify-content: center; align-items: center; min-height: calc(100vh - 56px - 48px); }
    .login-card { width: 100%; max-width: 400px; background: white; border-radius: 8px; padding: 32px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
    .login-card h2 { margin: 0 0 8px; color: #2e7d32; font-size: 22px; text-align: center; }
    .login-card .subtitle { text-align: center; color: rgba(0,0,0,0.54); font-size: 14px; margin-bottom: 24px; }
    .alert-error { padding: 10px; border-radius: 4px; background: #ffebee; color: #f44336; font-size: 13px; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; }
    .form-group { display: flex; flex-direction: column; margin-bottom: 16px; }
    .form-group label { font-size: 13px; color: rgba(0,0,0,0.54); margin-bottom: 4px; }
    .form-group input { padding: 10px 12px; border: 1px solid #e0e0e0; border-radius: 4px; font-size: 14px; font-family: inherit; transition: border-color 0.2s; }
    .form-group input:focus { outline: none; border-color: #2e7d32; }
    .form-group input.ng-invalid.ng-touched { border-color: #f44336; }
    .form-error { color: #f44336; font-size: 12px; margin-top: 4px; }
    .btn { display: inline-flex; align-items: center; justify-content: center; gap: 8px; width: 100%; padding: 10px; border: none; border-radius: 4px; font-size: 14px; cursor: pointer; transition: background 0.2s; font-family: inherit; }
    .btn-primary { background: #2e7d32; color: white; }
    .btn-primary:hover:not(:disabled) { background: #1b5e20; }
    .btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }
    .spinner { display: inline-block; width: 16px; height: 16px; border: 2px solid rgba(255,255,255,0.3); border-top-color: white; border-radius: 50%; animation: spin 0.6s linear infinite; }
    @keyframes spin { to { transform: rotate(360deg); } }
    .register-link { text-align: center; margin-top: 16px; font-size: 13px; color: rgba(0,0,0,0.54); }
    .register-link a { color: #2e7d32; text-decoration: none; font-weight: 500; }
  `]
})
export class Login implements OnInit {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private auth = inject(AuthService);

  error = '';
  loading = false;

  form = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  ngOnInit() {
    if (this.auth.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  submit() {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    this.auth.login(this.form.value as any).subscribe({
      next: () => this.router.navigate(['/dashboard']),
      error: (err) => {
        this.error = err.error?.error || 'Usuario o contrasena incorrectos';
        this.loading = false;
      }
    });
  }
}
