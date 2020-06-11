import {EventCollision} from "./event-collision";

export class CollisionResponse {
  constructor(
    public eventCollisions: EventCollision[],
    public dateSuggestions: Date[],
  ) {
  }
}
