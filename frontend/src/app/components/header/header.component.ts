import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import { faSignInAlt , faSignOutAlt, faChevronDown } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService) { }

  ngOnInit() {
  }

  faSignInAlt = faSignInAlt;
  faSignOutAlt = faSignOutAlt;
  faChevronDown = faChevronDown;
}
