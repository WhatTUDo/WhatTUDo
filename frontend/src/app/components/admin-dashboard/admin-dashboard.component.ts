import {Component, OnInit} from '@angular/core';
import {
  faChevronLeft,
  faCog,
  faKey,
  faPlus,
  faRedoAlt,
  faTimes,
  faTimesCircle
} from "@fortawesome/free-solid-svg-icons";
import {LabelService} from "../../services/label.service";
import {Label} from "../../dtos/label";
import {UserService} from "../../services/user.service";
import {User} from "../../dtos/user";
import {ChangeUserPasswordDto} from "../../dtos/ChangeUserPasswordDto";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {

  faChevronLeft = faChevronLeft;
  faTimesCircle = faTimesCircle;
  faPlus = faPlus;
  faCog = faCog;
  faKey = faKey;
  faRedoAlt = faRedoAlt;
  faTimes = faTimes;
  labels: Label[];
  newLabelName: string;
  addLabelExpanded: boolean = false;
  users: User[];


  constructor(private labelService: LabelService,
              private userService: UserService) {
    this.loadAllLabels()
    this.getAllUsers()
  }

  ngOnInit(): void {
  }

  loadAllLabels() {
    this.labelService.getAll().subscribe((labels: Label[]) => {
      this.labels = labels;
    })
  }

  deleteLabel(id: number) {
    if (confirm(`You are deleting label "${this.labels.find(l => l.id === id).name}". Are you sure?`)) {
      this.labelService.deleteLabel(id).subscribe(_ => {
        this.labels = this.labels.filter(l => l.id !== id);
      });
    }
  }

  onSubmit() {
    this.labelService.createLabel(this.newLabelName).subscribe((label) => {
      this.labels.push(label);
    });
  }

  toggleAddLabelExpanded() {
    this.addLabelExpanded = !this.addLabelExpanded;
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe((users) => {
      this.users = users;
    })
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  generatePassword(): string {
    const c = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    return Array.apply(null, Array(4)).map(function () {
      return Array.apply(null, Array(3)).map(function () {
        return c.charAt(Math.random() * c.length);
      }).join('');
    }).join('-');
  }

  resetPassword(username: string) {
    const password = this.generatePassword();
    const changeUserPasswordDto = new ChangeUserPasswordDto(username, null, null, password);
    this.userService.changePwd(changeUserPasswordDto).subscribe((_) => {
      alert(`Password of "${username}" changed to: \n${password}`)
    })
  }
}
