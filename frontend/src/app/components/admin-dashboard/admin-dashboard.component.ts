import {Component, OnInit} from '@angular/core';
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {

  faChevronLeft = faChevronLeft;

  constructor() {
  }

  ngOnInit(): void {
  }

}
