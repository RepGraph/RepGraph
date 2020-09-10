import React from 'react';
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import { makeStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';
import DropZoneUploadDataset from "../UploadDatasetDialogue";

const useStyles = makeStyles((theme) => ({
    root: {
        marginBottom:10,
    },
    menuButton: {
        marginRight: theme.spacing(2),
    },
    title: {
        flexGrow: 1,
    }
}));

function Header(){
    const classes = useStyles();
    return (
        <div className={classes.root}>
            <AppBar position="static" >
                <Toolbar >
                    <IconButton edge="start" className={classes.menuButton} color="inherit" aria-label="menu">
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" className={classes.title}>
                        RepGraph
                    </Typography>
                    <DropZoneUploadDataset></DropZoneUploadDataset>
                </Toolbar>
            </AppBar>
        </div>
    );

}
export default Header;
