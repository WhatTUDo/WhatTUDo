import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faTimesCircle, faPlus, faCog} from "@fortawesome/free-solid-svg-icons";
import {LabelService} from "../../services/label.service";
import {Label} from "../../dtos/label";

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
  labels: Label[];

  constructor(private labelService: LabelService) {
    this.loadAllLabels()
  }

  ngOnInit(): void {
  }

  loadAllLabels() {
    this.labelService.getAll().subscribe((labels: Label[]) => {
      this.labels = labels;
    })
  }

  deleteLabel(id:number){
    this.labelService.deleteLabel(id).subscribe(_=>{});
  }
}
