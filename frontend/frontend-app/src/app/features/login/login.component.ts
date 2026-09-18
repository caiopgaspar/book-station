import { Component, ChangeDetectorRef } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/auth/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials = {
    username: '',
    password: ''
  };
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {}

  onSubmit(): void {
    console.log('Credencials:', this.credentials);
    if (!this.credentials.username || !this.credentials.password) {
      this.toastr.error('Please fill in all the fields.');
      return;
    }

    this.loading = true;
    this.authService.login(this.credentials).subscribe({
      next: () => {
        console.log('Login successful:', Response);
        this.toastr.success('Login successful!');
        this.router.navigate(['/books']);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error logging in:', error);
        console.error('Status:', error.status);
        console.error('Message:', error.error?.message || error.message);
        this.loading = false;
        this.toastr.error(error.error?.message || 'Error logging in.');
        this.cdr.detectChanges();
      }
    });
  }
}
