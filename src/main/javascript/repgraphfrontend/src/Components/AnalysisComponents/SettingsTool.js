import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import ListSubheader from '@material-ui/core/ListSubheader';
import Switch from '@material-ui/core/Switch';
import WifiIcon from '@material-ui/icons/Wifi';
import BluetoothIcon from '@material-ui/icons/Bluetooth';

function SettingsTool(props) {



    return ( <List subheader={<ListSubheader>Settings</ListSubheader>} >
            <ListItem>
                <ListItemIcon>
                    <WifiIcon />
                </ListItemIcon>
                <ListItemText primary="Wi-Fi" />
                <ListItemSecondaryAction>
                    <Switch
                    />
                </ListItemSecondaryAction>
            </ListItem>
            <ListItem>
                <ListItemIcon>
                    <BluetoothIcon />
                </ListItemIcon>
                <ListItemText primary="Bluetooth" />
                <ListItemSecondaryAction>
                    <Switch


                    />
                </ListItemSecondaryAction>
            </ListItem>
        </List>
    );
}

export default SettingsTool;