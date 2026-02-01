import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class LoginComponent implements AfterViewInit {
  constructor(private router: Router) {}

  ngAfterViewInit() {
    // Load Google Sign-In script
    this.loadGoogleScript();
  }

  loadGoogleScript() {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    script.onload = () => {
      this.initializeGoogleSignIn();
    };
    document.head.appendChild(script);
  }

  initializeGoogleSignIn() {
    (window as any).google.accounts.id.initialize({
      client_id: environment.googleClientId,
      callback: this.handleCredentialResponse.bind(this)
    });

    (window as any).google.accounts.id.renderButton(
      document.getElementById('google-signin-button'),
      { theme: 'filled_blue', size: 'large', text: 'signin_with', shape: 'rectangular' }
    );
  }

  handleCredentialResponse(response: any) {
    console.log('Google Token:', response.credential);
    
    // Decode JWT token to get user info
    const userInfo = this.decodeJWT(response.credential);
    console.log('User Info:', userInfo);
    
    // Send token and user info to backend
    fetch(`${environment.apiUrl}/auth/google`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        token: response.credential,
        email: userInfo.email,
        name: userInfo.name,
        picture: userInfo.picture
      })
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(data.user));
        this.router.navigate(['/dashboard']);
      }
    })
    .catch(err => console.error('Login error:', err));
  }
  
  decodeJWT(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (err) {
      console.error('Error decoding JWT:', err);
      return {};
    }
  }

}
