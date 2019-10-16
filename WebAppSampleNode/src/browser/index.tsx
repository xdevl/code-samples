import Button from "@material-ui/core/Button";
import Card from "@material-ui/core/Card";
import CardActions from "@material-ui/core/CardActions";
import CardContent from "@material-ui/core/CardContent";
import CardHeader from "@material-ui/core/CardHeader";
import TouchIcon from "@material-ui/icons/TouchApp";
import React from "react";
import ReactDOM from "react-dom";
import { Info } from "../model/info";

export const api = <T, >(url: string): Promise<T> => {
    return fetch(url)
      .then((res) => res.ok ? res.json() as Promise<T> : (() => {
        throw new Error(`${res.status} ${res.statusText}`);
      })());
  };

function Main() {
    return <Card className="center" style={{width: "600px"}}>
        <CardHeader title="Web App Sample" />
        <CardContent>
            Click on the button below to call the backend API
        </CardContent>
        <CardActions>
            <Button variant="contained" color="primary" startIcon={<TouchIcon />}
                    onClick={() => api<Info>("/info").then((info) => alert(`app version: ${info.version}`))}>
                Click me
            </Button>
        </CardActions>
    </Card>;
}

ReactDOM.render(<Main />, document.querySelector("#root"));
