import GeneralStore from "./GeneralStore";
import {action, computed, decorate, observable} from "mobx";

export class RoomStore extends GeneralStore {

    _rooms = new Map();

    _roomService;

    constructor(roomService) {
        super();
        this._roomService = roomService;
    }

    setRooms = (roomsList) => {
        for (let i = 0; i < roomsList.length; i++) {
            this._rooms.set(roomsList[i].id, roomsList[i]);
        }
    };

    setRoom = (room) => {
        this._rooms.set(room.id, room);
    };

    get rooms() {
        return this._rooms;
    }

    getAllRooms = (homeID) => {
        this.startLoading();
        this._roomService
            .getAllRooms(homeID)
            .then(this.setRooms)
            .catch(this.setError)
            .finally(this.stopLoading);
    };

    getRoomByID = (homeID, roomID) => {
        this.startLoading();
        this._roomService
            .getRoomByID(homeID, roomID)
            .then(this.setRoom)
            .catch(this.setError)
            .finally(this.stopLoading);
    };

    createRoom = (homeID, room) => {
        this.startLoading();
        this._roomService
            .createRoom(homeID, room)
            .then(this.setRoom)
            .catch(this.setError)
            .finally(this.stopLoading);
    };

    addRoomToHome = (homeID, roomID) => {
        this.startLoading();
        this._roomService
            .addRoomToHome(homeID, roomID)
            .then(this.setRooms)
            .catch(this.setError)
            .finally(this.stopLoading);
    };

    updateRoom = (homeID, roomID, room) => {
        this.startLoading();
        this._roomService
            .updateRoom(homeID, roomID, room)
            .then(this.setRoom)
            .catch(this.setError)
            .finally(this.stopLoading);
    };

    deleteRoom = (homeID, roomID) => {
        this.startLoading();
        this._roomService
            .deleteRoom(homeID, roomID)
            .catch(this.setError)
            .finally(this.stopLoading);
    };
}

decorate(RoomStore, {
    _rooms: observable,

    setRoom: action,
    setRooms: action,

    rooms: computed
})
;