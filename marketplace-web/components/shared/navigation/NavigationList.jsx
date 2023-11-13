import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Drawer } from 'antd';
import PanelMenu from '../panel/PanelMenu';
import PanelCartMobile from '../panel/PanelCartMobile';
import PanelSearch from '../panel/PanelSearch';
import PanelCategories from '../panel/PanelCategories';
import { Router, useRouter } from 'next/router';



import { Menu } from 'antd';
import { menuPrimary } from '~/public/static/data/menu';
import menu_data from '~/public/static/data/menu';
import Link from 'next/link';
const { SubMenu } = Menu;

class NavigationList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            menuDrawer: false,
            cartDrawer: false,
            searchDrawer: false,
            categoriesDrawer: false,
            keyword:'',

        };
    }

   
    

    handleDrawerClose() {

        this.setState({
            menuDrawer: false,
            cartDrawer: false,
            searchDrawer: false,
            categoriesDrawer: false,
        });
    };

    handleShowMenuDrawer = () => {
        this.setState({
            menuDrawer: !this.state.menuDrawer,
            cartDrawer: false,
            searchDrawer: false,
            categoriesDrawer: false,
        });
    };

    handleShowCartDrawer = () => {
        this.setState({
            menuDrawer: false,
            cartDrawer: !this.state.cartDrawer,
            searchDrawer: false,
            categoriesDrawer: false,
        });
    };
    handleShowSearchDrawer = () => {
        this.setState({
            menuDrawer: false,
            cartDrawer: false,
            searchDrawer: !this.state.searchDrawer,
            categoriesDrawer: false,
        });
    };
    handleShowCategoriesDrawer = () => {
        this.setState({
            menuDrawer: false,
            cartDrawer: false,
            searchDrawer: false,
            categoriesDrawer: !this.state.categoriesDrawer,
        });
    };

    render() {
        const {
            menuDrawer,
            searchDrawer,
            cartDrawer,
            categoriesDrawer,
        } = this.state;

        return (
            <div className="navigation--list">
                <Drawer
                    className="ps-panel--mobile"
                    placement="right"
                    closable={false}
                    onClose={this.handleDrawerClose}
                    visible={this.state.menuDrawer}>
                    <div className="ps-panel--wrapper">
                        <div className="ps-panel__header">
                            <h3>Menu</h3>
                            <span
                                className="ps-panel__close"
                                onClick={() => { this.handleDrawerClose() }}>
                                <i className="icon-cross"></i>
                            </span>
                        </div>
                        <div className="ps-panel__content">
                            
            <Menu
                mode="inline"
                openKeys={this.state.openKeys}
                onOpenChange={this.onOpenChange}
                className="menu--mobile-2">
                {menu_data.menuPrimary.menu_1.map((item) => {
                    if (item.subMenu) {
                        return (
                            <SubMenu
                                key={item.text}
                                title={
                                    <Link href={item.url}>
                                        <a>{item.text}</a>
                                    </Link>
                                }>
                                {item.subMenu.map((subItem) => (
                                    <Menu.Item key={subItem.text}>
                                        <Link href={subItem.url}>
                                            <h6 style={{fontSize:"14px",fontWeight:"100",padding:"12px 0px"}} onClick={()=>{this.handleDrawerClose()}}>{subItem.text}</h6>
                                        </Link>
                                    </Menu.Item>
                                ))}
                            </SubMenu>
                        );
                    } else if (item.megaContent) {
                        return (
                            <SubMenu
                                key={item.text}
                                title={
                                    <Link href={item.url}>
                                        <a>{item.text}</a>
                                    </Link>
                                }>
                                {item.megaContent.map((megaItem) => (
                                    <SubMenu
                                        key={megaItem.heading}
                                        title={<span>{megaItem.heading}</span>}>
                                        {megaItem.megaItems?.map(
                                            (megaSubItem) => (
                                                <Menu.Item
                                                    key={megaSubItem.text}>
                                                    <Link href={megaSubItem.url}>
                                                        <h6 style={{fontSize:"14px",padding:"12px 0px"}}  onClick={()=>{this.handleDrawerClose()}}>
                                                            {megaSubItem.text}
                                                        </h6>
                                                    </Link>
                                                </Menu.Item>
                                            )
                                        )}
                                    </SubMenu>
                                ))}
                            </SubMenu>
                        );
                    } else {
                        return (
                            <Menu.Item key={item.text}>
                                {item.type === 'dynamic' ? (
                                    <Link
                                        href={`${item.url}/[pid]`}
                                        as={`${item.url}/${item.endPoint}`}>
                                        <a onClick={()=>{this.handleDrawerClose()}}>{item.text}</a>
                                    </Link>
                                ) : (
                                    <Link href={item.url} as={item.alias}>
                                        <h5 style={{fontSize:"17px",padding:"20px 0px"}} onClick={()=>{this.handleDrawerClose()}}>{item.text}</h5>
                                    </Link>
                                )}
                            </Menu.Item>
                        );
                    }
                })}
            </Menu>
        

                        </div>
                    </div>
                </Drawer>
                <Drawer
                    className="ps-panel--mobile"
                    placement="right"
                    closable={false}
                    onClose={this.handleDrawerClose}
                    visible={this.state.cartDrawer}>
                    <div className="ps-panel--wrapper">
                        <div className="ps-panel__header">
                            <h3>Shopping Cart</h3>
                            <span
                                className="ps-panel__close"
                                onClick={() => { this.handleDrawerClose() }}>
                                <i className="icon-cross"></i>
                            </span>
                        </div>
                        <div className="ps-panel__content">
                            <PanelCartMobile />
                        </div>
                    </div>
                </Drawer>
                <Drawer
                    className="ps-panel--mobile"
                    placement="right"
                    closable={false}
                    onClose={this.handleDrawerClose}
                    visible={this.state.searchDrawer}>
                    <div className="ps-panel--wrapper">
                        <div className="ps-panel__header">
                            <h3>Search</h3>
                            <span
                                className="ps-panel__close"
                                onClick={() => { this.handleDrawerClose() }}>
                                <i className="icon-cross"></i>
                            </span>
                        </div>
                        <div className="ps-panel__content">
                            

                            <div className="ps-panel__search-results">
                                
                                    <div className="form-group--nest">
                                        <input
                                            className="form-control"
                                            type="text"
                                            placeholder="Search something ..."
                                            onChange={(e) => this.setState({keyword : e.target.value})}
                                        />
                                        <Link href={`/search?keyword=${this.state.keyword}`}>
                                            <button onClick={() => { this.handleDrawerClose() }}>
                                                <i className="icon-magnifier"></i>
                                            </button>
                                        </Link>
                                        
                            
                                    </div>
                            
                            </div>

                        </div>
                    </div>
                </Drawer>
                <Drawer
                    style={{width:"60%"}}
                    className="ps-panel--mobile"
                    placement="left"
                    closable={false}
                    onClose={this.handleDrawerClose}
                    visible={this.state.categoriesDrawer}>
                    <div className="ps-panel--wrapper">
                        <div className="ps-panel__header">
                            <h3>Categories</h3>
                            <span
                                className="ps-panel__close"
                                onClick={() => { this.handleDrawerClose() }}>
                                <i className="icon-cross"></i>
                            </span>
                        </div>
                        <div className="ps-panel__content">
                            <PanelCategories  />
                        </div>
                    </div>
                </Drawer>
                <div className="navigation__content">
                    <a
                        className={`navigation__item ${menuDrawer === true ? 'active' : ''
                            }`}
                        onClick={this.handleShowMenuDrawer}>
                        <i className="icon-menu"></i>
                        <span> Menu</span>
                    </a>
                    <a
                        className={`navigation__item ${categoriesDrawer === true ? 'active' : ''
                            }`}
                        onClick={this.handleShowCategoriesDrawer}>
                        <i className="icon-list4"></i>
                        <span> Categories</span>
                    </a>
                    <a
                        className={`navigation__item ${searchDrawer === true ? 'active' : ''
                            }`}
                        onClick={this.handleShowSearchDrawer}>
                        <i className="icon-magnifier"></i>
                        <span> Search</span>
                    </a>
                    <a
                        className={`navigation__item ${cartDrawer === true ? 'active' : ''
                            }`}
                        onClick={this.handleShowCartDrawer}>
                        <i className="icon-bag2"></i>
                        <span> Cart</span>
                    </a>
                </div>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return state.setting;
};
export default connect(mapStateToProps)(NavigationList);
