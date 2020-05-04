import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  currentTime: string;

  constructor(public authService: AuthService) { }

  ngOnInit() {
    this.updateDatetime();
    setInterval(_=>{this.updateDatetime();}, 1000);
  }

  updateDatetime() {
    const date = new Date(Date.now());
    this.currentTime = date.toLocaleString('en-US', { hour: 'numeric', minute: 'numeric', hour12: true })
  }

}
