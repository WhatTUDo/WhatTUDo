import {AfterContentChecked,ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-edit-user',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.scss']
})
export class UserFormComponent implements OnInit, AfterContentChecked {
  updateForm:FormGroup;
  constructor(private formBuilder: FormBuilder, private cd: ChangeDetectorRef) {
    this.updateForm=this.formBuilder.group(
      {
        username: new FormControl('', Validators.max(255)),
        email: new FormControl('',[
          Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$")]),
        currentPassword: new FormControl(''),
        newPassword: new FormControl('')

      }
    );
  }
  ngOnInit(): void {
  }

  public update() {
    if(this.updateForm.valid) {
      if (this.updateForm.controls.newPassword.value != null){
        if(this.updateForm.controls.currentPassword.value != null){
          //do password check
        }else {
          alert("Please enter your current password!")
        }
      }
      //call update method in service.
        }
  }

  public clearForm(){
    this.updateForm.reset();
  }
  ngAfterContentChecked() {
    this.cd.detectChanges();
  }


}
