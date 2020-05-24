import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import { faUserCircle, faChevronDown } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  faUserCircle = faUserCircle;
  faChevronDown = faChevronDown;

  logMeIn(){
    const loginData = {
      username: "Person 1",
      password: "password"
    }

    fetch('//localhost:8080/api/v1/authentication', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginData)
    }).then(response => response.text()).then(data => {
      const token = data.split(' ')[1]
      localStorage.setItem('authToken', token)
    }).catch(_ => alert("Login failed"))
  }
}
