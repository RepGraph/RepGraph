import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import Switch from '@material-ui/core/Switch';
import WifiIcon from '@material-ui/icons/Wifi';
import BluetoothIcon from '@material-ui/icons/Bluetooth';
import PaletteIcon from '@material-ui/icons/Palette';
import {PresentToAll} from "@material-ui/icons";
import {Slider, TextField} from "@material-ui/core";
import Grid from "@material-ui/core/Grid";
import LinearScaleIcon from '@material-ui/icons/LinearScale';
import FormatLineSpacingIcon from '@material-ui/icons/FormatLineSpacing';

function SettingsTool(props) {


    return (<List subheader={<ListSubheader>Settings</ListSubheader>}>
            <ListItem>
                <ListItemIcon>
                    <PaletteIcon/>
                </ListItemIcon>
                <ListItemText primary="Abstract Node Colour"/>
                <ListItemSecondaryAction>
                    <TextField value={"#"}/>
                </ListItemSecondaryAction>
            </ListItem>
            <ListItem>
                <ListItemIcon>
                    <PaletteIcon/>
                </ListItemIcon>
                <ListItemText primary="Surface Node Colour"/>
                <ListItemSecondaryAction>
                    <TextField value={"#"}/>
                </ListItemSecondaryAction>
            </ListItem>
            <ListItem>
                <ListItemIcon>
                    <LinearScaleIcon/>
                </ListItemIcon>
                <ListItemText primary="Intralevel Node Spacing"/>
                <ListItemSecondaryAction>
                    <TextField />
                </ListItemSecondaryAction>
            </ListItem>
            <ListItem>
                <ListItemIcon>
                    <FormatLineSpacingIcon/>
                </ListItemIcon>
                <ListItemText primary="Interlevel Node Spacing"/>
                <ListItemSecondaryAction>
                    <TextField />
                </ListItemSecondaryAction>
            </ListItem>

        </List>
    );
}

export default SettingsTool;