import express from "express";
import NodePath from "path";
import { Info } from "../model/info";

declare const VERSION: string;
const port = 8081;

express().use(express.static(NodePath.join(__dirname, "browser")))
    .get("/info", (req, res) => res.json(new Info(VERSION)))
    .listen(port, () => {
        // tslint:disable-next-line:no-console
        console.log(`server started at http://localhost:${ port }`);
    });
