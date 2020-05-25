import {AfterContentChecked, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit, AfterContentChecked {
  updateForm: FormGroup;
  changePwdForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private cd: ChangeDetectorRef,
              private route: ActivatedRoute) {
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

  ngAfterContentChecked() {
    this.cd.detectChanges();
  }


}