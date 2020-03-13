import React from "react";
import {useHistory, useParams, useRouteMatch} from "react-router-dom";
import {useObserver} from "mobx-react-lite";
import useStores from "../../hooks/useStores";
import GridContainer from "../../components/Grid/GridContainer";
import MainDisplayCard from "../../components/BartCard/MainDisplayCard";
import CardIcon from "components/Card/CardIcon.js";
import NoItemsAvailable from "../../components/BartCard/NoItemsAvailable";
import AddCard from "../../components/BartCard/AddCard";

const filterRooms = (rooms, idHome) => {
    let tmp = new Map();
    [...rooms].map(([key, item], index) => {
        if (item.homeID === idHome) {
            tmp.set(key, item);
        }
    });
    return tmp;
};

export default function Home(props) {
    const {homeStore, roomStore} = useStores();
    const {homeID} = useParams();
    const {path} = useRouteMatch();
    const history = useHistory();
    const id = parseInt(homeID || -1);

    React.useEffect(() => {
        if (!homeStore.homes[id]) {
            homeStore.getHomeByID(id);
            roomStore.getAllRooms(id);
        }
    }, [homeStore, roomStore, id]);

    return useObserver(() => {
        const {error, loading, homes} = homeStore;
        const {rooms} = roomStore;

        const allRooms = [...filterRooms(rooms, id)]
            .map(([key, item], index) => (
                <MainDisplayCard key={index} id={item.id} title={item.name} color={CardIcon.getColorID(index)}/>
            ));

        const printAllRooms = allRooms.length === 0 ? <NoItemsAvailable message={"No Rooms found"}/> : allRooms;

        return (
            <div>
                <GridContainer>
                    {printAllRooms}
                    <AddCard title="Add Room" color="success"/>
                </GridContainer>
            </div>
        );
    });

}