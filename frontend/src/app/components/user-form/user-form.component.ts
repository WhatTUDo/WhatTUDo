import {AfterContentChecked, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {User} from "../../dtos/user";
import {AuthService} from "../../services/auth.service";
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";
import {FeedbackService} from "../../services/feedback.service";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit, AfterContentChecked {
  updateForm: FormGroup;
  changePwdForm: FormGroup;

  user: User;
  faChevronLeft = faChevronLeft;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private authService: AuthService,
              private feedbackService: FeedbackService,
              private cd: ChangeDetectorRef,
              private route: ActivatedRoute,
              private router: Router) {
    this.updateForm = this.formBuilder.group(
      {
        username: new FormControl('', Validators.max(255)),
        email: new FormControl('', [
          Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]),
      }
    );
    this.changePwdForm = this.formBuilder.group(
      {
        currentPassword: new FormControl('', Validators.required),
        newPassword: new FormControl('', Validators.required)
      }
    );
    const id = +this.route.snapshot.paramMap.get('id');
    // if (id) {
    //   this.userService.getById(id).subscribe((user: User) => {
    //     this.user = user;
    //   });
    // } else {
    if (this.authService.isLoggedIn()) {
      this.authService.getUser().subscribe((user: User) => {
        this.user = user;
      });
    } else {
      this.feedbackService.displayError('Login Required.', 'You can only do this after you logged in.');
    }
  }

  ngOnInit(): void {
  }

  public update() {
    if (this.updateForm.valid) {
      //call update method in service.
    }
  }

  public changePassword() {
    if (this.changePwdForm.valid) {
      //call update method in service.
    }
  }

  public clearForm() {
    this.updateForm.reset();
    this.changePwdForm.reset();
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  ngAfterContentChecked() {
    this.cd.detectChanges();
  }

  emailUpdated(event: Event) {
    @ts-ignore
    this.user.email = event.target.value;
  }
}
