import {Component, OnInit} from '@angular/core';
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";
import {User} from "../../dtos/user";
import {UserService} from "../../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.scss']
})
export class UserRegistrationComponent implements OnInit {

  user: User = new User(null, null, null);
  password: string;
  gravatarLink: string;
  faChevronLeft = faChevronLeft;

  constructor(private userService: UserService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.gravatarLink = this.userService.getGravatarLink(null, 256);
  }

  emailUpdated() {
    this.gravatarLink = this.userService.getGravatarLink(this.user.email, 256);
  }

  onSubmit() {
    this.userService.postUser({...this.user, password: this.password}).subscribe((_) => {
      this.router.navigate(['/user'])
    })
  }
}
