import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
})
export class DashboardComponent implements OnInit {
  user: any = null;
  allUsers: any[] = [];
  loading: boolean = true;

  constructor(private router: Router) {}

  ngOnInit() {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      this.user = JSON.parse(userStr);
    } else {
      this.router.navigate(['/login']);
      return;
    }
    
    this.loadAllUsers();
  }
  
  loadAllUsers() {
    this.loading = true;
    fetch(`${environment.apiUrl}/auth/users`)
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          this.allUsers = data.users || [];
        }
        this.loading = false;
      })
      .catch(err => {
        console.error('Error loading users:', err);
        this.loading = false;
      });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

}
