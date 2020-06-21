import {Component, OnInit} from '@angular/core';
import {faChevronLeft, faChevronRight, faCog, faPlus, faTimes, faTimesCircle} from '@fortawesome/free-solid-svg-icons'
import {Organization} from "../../dtos/organization";
import {ActivatedRoute} from "@angular/router";
import {OrganizationService} from "../../services/organization.service";
import {CalendarService} from "../../services/calendar.service";
import {Calendar} from "../../dtos/calendar";
import {User} from "../../dtos/user";
import {UserService} from "../../services/user.service";
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {FeedbackService} from '../../services/feedback.service';
import {Globals} from "../../global/globals";

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {

  organization: Organization;
  organizationCalendars: Calendar[] = [];
  organizationMembers: User[];
  editableCalendars: Calendar[];
  roles : string[] = ["Moderator", "Member"];
  pickedCalendarId: number;
  pickedRole: string;
  calendarAddExpanded: boolean = false;
  userAddExpanded : boolean = false;
  faChevronLeft = faChevronLeft;
  faChevronRight = faChevronRight;
  faPlus = faPlus;
  faTimes = faTimes;
  faCog = faCog;
  faTimesCircle = faTimesCircle;
  addMemberForm = new FormGroup({
    username: new FormControl(''),
    role: new FormControl('')
  });

  constructor(private organizationService: OrganizationService,
              private calendarService: CalendarService,
              private userService: UserService,
              private feedbackService: FeedbackService,
              private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private globals: Globals) {
    let id: number = Number(this.route.snapshot.paramMap.get('id'));
    this.loadOrganization(id);
    this.getAllEditableCalendars();
    this.addMemberForm = this.formBuilder.group({
      username: new FormControl('', Validators.required),
      role: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
  }
  onSubmitAddMember(){
    this.addMembership();
    this.userAddExpanded = false;
  }
  onSubmitAddCalendar(calId: number) {
    this.addCalendar(calId);
    this.calendarAddExpanded = false;
    delete this.pickedCalendarId;
  }

  addCalendar(calId: number) {
    console.log('Cal ID' + calId);
    this.organizationService.addCalendarToOrga(this.organization.id, calId).subscribe((organization: Organization) => {
      this.organization = organization;
      this.calendarService.getCalendarById(calId).subscribe((cal: Calendar) => {
        this.organizationCalendars.push(cal);
      })
    }, err => {
      alert(err.message);
    });

  }

  selectCalendar(calId: number) {
    this.pickedCalendarId = calId;
  }

  selectRole(role: string){
    this.pickedRole = role;
  }

  removeCalendar(calId: number) {
    if (confirm(`You are deleting calendar "${this.organizationCalendars.find(c => c.id === calId).name}". Are you sure?`)) {
      this.organizationService.removeCalendarToOrga(this.organization.id, calId).subscribe((organization: Organization) => {
        this.organization = organization;
        this.organizationCalendars = this.organizationCalendars.filter((cal: Calendar) => {
          return cal.id != calId
        })
      });
    }
  }

  getAllEditableCalendars() {
    this.calendarService.getAllCalendars().subscribe((calendars: Calendar[]) => {
      this.editableCalendars = calendars;
    }) //FIXME: Make me to fetch only editable calendars.
  }

  /**
   * Loads Organization with ID from Service.
   * @param id
   */
  private loadOrganization(id: number) {
    this.organizationService.getById(id).subscribe((organization: Organization) => {
      this.organization = organization;
      this.organization.coverImageUrl = this.globals.backendUri+this.organization.coverImageUrl;
      for (let calID of organization.calendarIds) {
        this.calendarService.getCalendarById(calID).subscribe((cal: Calendar) => {
          this.organizationCalendars.push(cal);
        });
      }
      this.organizationService.getMembers(organization.id).subscribe((users: User[]) => {
        this.organizationMembers = users;
      });
    })
  }

  getGravatarLink(email, size) {
    return this.userService.getGravatarLink(email, size);
  }

  deleteOrganization(id: number) {
    if (confirm(`You are deleting organization "${this.organization.name}". Are you sure?`)) {
      this.organizationService.deleteOrganization(id).subscribe(organization => {
      })
    }
  }

  removeFromOrg(userId: number, organizationId: number) {
    if (
      confirm(`You are removing "${
        this.organizationMembers.find(m => m.id === userId).name
      }" from "${
        this.organization.name
      }". Are you sure?`)
    ) {
      this.userService.removeFromOrganization(userId, organizationId).subscribe((user) => {
      })
    }
  }

  addMembership(){
    this.userService.getUserByName(this.addMemberForm.controls.username.value).subscribe((found:User)=>{
      this.organizationService.addMembership( this.organization.id,found.id, this.addMemberForm.controls.role.value).subscribe((orga: any) => {
          this.organization = orga;
          this.organizationMembers.push(found);
          this.feedbackService.displaySuccess("Success", "User added as a "+this.addMemberForm.controls.role.value)
        }, error =>
          this.feedbackService.displayError("Error", error.error.message )
      );
    },error => {
      this.feedbackService.displayError("Error", error.error.message )
    });

    }
}
