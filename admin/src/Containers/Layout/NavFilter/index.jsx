import React from 'react';
import { Dropdown } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import { IconCalendar2X, IconCancelDocument, IconFileCheck } from '@Components/Icon';
import { NavLink, useHistory, useLocation } from "react-router-dom";
function NavFilter(props) {
  const statistics = useSelector(state => state.data.statistics);
  const desktopMenu = useSelector(state => state.menu.menu);
  const mobileMenu = desktopMenu.filter(m => m.id <= 4);
  const otherMenu = desktopMenu.filter(m => m.id > 4);
  const tags = useSelector(state => state.data.tags);
  const history = useHistory();
  const mapIconMenu = data => {
    switch (data.toLowerCase()) {
      case 'tất cả hợp đồng':
        return (
          <svg width="1em" height="1em" viewBox="0 0 16 16" className="bi bi-list-task" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path fillRule="evenodd" d="M2 2.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5V3a.5.5 0 0 0-.5-.5H2zM3 3H2v1h1V3z" />
            <path d="M5 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zM5.5 7a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9zm0 4a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9z" />
            <path
              fillRule="evenodd"
              d="M1.5 7a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H2a.5.5 0 0 1-.5-.5V7zM2 7h1v1H2V7zm0 3.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5H2zm1 .5H2v1h1v-1z"
            />
          </svg>
        );
      case 'chờ xử lí':
        return (
          <svg width="1em" height="1em" viewBox="0 0 16 16" className="bi bi-list-task" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path fillRule="evenodd" d="M2 2.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5V3a.5.5 0 0 0-.5-.5H2zM3 3H2v1h1V3z" />
            <path d="M5 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zM5.5 7a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9zm0 4a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9z" />
            <path
              fillRule="evenodd"
              d="M1.5 7a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H2a.5.5 0 0 1-.5-.5V7zM2 7h1v1H2V7zm0 3.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5H2zm1 .5H2v1h1v-1z"
            />
          </svg>
        );
      case 'chờ đối tác':
        return (
          <svg width="1em" height="1em" viewBox="0 0 16 16" className="bi bi-list-task" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path fillRule="evenodd" d="M2 2.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5V3a.5.5 0 0 0-.5-.5H2zM3 3H2v1h1V3z" />
            <path d="M5 3.5a.5.5 0 0 1 .5-.5h9a.5.5 0 0 1 0 1h-9a.5.5 0 0 1-.5-.5zM5.5 7a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9zm0 4a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9z" />
            <path
              fillRule="evenodd"
              d="M1.5 7a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H2a.5.5 0 0 1-.5-.5V7zM2 7h1v1H2V7zm0 3.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5H2zm1 .5H2v1h1v-1z"
            />
          </svg>
        );
      case 'sai xác thực':
        return (
          <svg width="1em" height="1em" viewBox="0 0 16 16" className="bi bi-shield-x" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path
              fillRule="evenodd"
              d="M5.443 1.991a60.17 60.17 0 0 0-2.725.802.454.454 0 0 0-.315.366C1.87 7.056 3.1 9.9 4.567 11.773c.736.94 1.533 1.636 2.197 2.093.333.228.626.394.857.5.116.053.21.089.282.11A.73.73 0 0 0 8 14.5c.007-.001.038-.005.097-.023.072-.022.166-.058.282-.111.23-.106.525-.272.857-.5a10.197 10.197 0 0 0 2.197-2.093C12.9 9.9 14.13 7.056 13.597 3.159a.454.454 0 0 0-.315-.366c-.626-.2-1.682-.526-2.725-.802C9.491 1.71 8.51 1.5 8 1.5c-.51 0-1.49.21-2.557.491zm-.256-.966C6.23.749 7.337.5 8 .5c.662 0 1.77.249 2.813.525a61.09 61.09 0 0 1 2.772.815c.528.168.926.623 1.003 1.184.573 4.197-.756 7.307-2.367 9.365a11.191 11.191 0 0 1-2.418 2.3 6.942 6.942 0 0 1-1.007.586c-.27.124-.558.225-.796.225s-.526-.101-.796-.225a6.908 6.908 0 0 1-1.007-.586 11.192 11.192 0 0 1-2.417-2.3C2.167 10.331.839 7.221 1.412 3.024A1.454 1.454 0 0 1 2.415 1.84a61.11 61.11 0 0 1 2.772-.815z"
            />
            <path
              fillRule="evenodd"
              d="M6.146 6.146a.5.5 0 0 1 .708 0L8 7.293l1.146-1.147a.5.5 0 1 1 .708.708L8.707 8l1.147 1.146a.5.5 0 0 1-.708.708L8 8.707 6.854 9.854a.5.5 0 0 1-.708-.708L7.293 8 6.146 6.854a.5.5 0 0 1 0-.708z"
            />
          </svg>
        );
      case 'hoàn thành':
        return <IconFileCheck />;
      default:
        return;
    }
  };

  const getStatusCount = status => {
    let count = 0;
    if (statistics) {
      if (status == 'ALL') {
        statistics.forEach(s => {
          count += s.count;
        });
      } else {
        const arr = statistics.filter(s => s.status == status);
        if (arr.length > 0) {
          count += arr[0].count;
        }
      }
    }
    return count;
  };

  const mapIconOtherMenu = data => {
    switch (data.toLowerCase()) {
      case 'hủy bỏ':
        return <IconCancelDocument />;
      case 'hết hạn':
        return <IconCalendar2X />;
      default:
        return;
    }
  };
  function listContractTag(tag) {
    const params = {
      tagId: tag.id
    };
    Object.keys(params).forEach(key => {
      if (!params[key]) {
        delete params[key];
      }
    });
    history.push('/hop-dong-theo-the?' + new URLSearchParams(params).toString());
  }

  return (
    <>
      <div id="nav_mobi" className="container-fluid d-block d-lg-none">
        <div className="row">
          {mobileMenu.map((menu, index) => (
            <React.Fragment key={index}>
              {
                menu.status != 'TOTAL' &&
                <div className="col p-0">
                  <NavLink to={menu.path} exact={true} className="text-center nm_item">
                    {mapIconMenu(`${menu.title}`)}
                    <span className="mobi_count">{getStatusCount(menu.status)}</span>
                    <br />
                    <span>{menu.title}</span>
                  </NavLink>
                </div>
              }
            </React.Fragment>
          )
          )}
          <div className="col p-0">
            <Dropdown>
              <Dropdown.Toggle as={CustomToggle}>
                <svg width="1em" height="1em" viewBox="0 0 16 16" className="bi bi-three-dots" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                  <path
                    fillRule="evenodd"
                    d="M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z"
                  />
                </svg>
                <span className="mobi_count">{tags ? tags.length + 2 : 2}</span>
                <br />
                <span>Khác</span>
              </Dropdown.Toggle>
              <Dropdown.Menu className="dropdown-menu-right dropdown-menu-other shadow" popperConfig={{ placement: 'end-top' }}>
                {tags &&
                  tags.map((tag, index) => (
                    <a onClick={() => listContractTag(tag)} className="dropdown-item" key={index}>
                      <svg className="flip_hor" viewBox="0 0 24 24" fill={tag.colorCode} width="22px" height="22px">
                        <path d="M0 0h24v24H0V0z" fill="none" />
                        <path d="M21.41 11.58l-9-9C12.05 2.22 11.55 2 11 2H4c-1.1 0-2 .9-2 2v7c0 .55.22 1.05.59 1.42l9 9c.36.36.86.58 1.41.58s1.05-.22 1.41-.59l7-7c.37-.36.59-.86.59-1.41s-.23-1.06-.59-1.42zM5.5 7C4.67 7 4 6.33 4 5.5S4.67 4 5.5 4 7 4.67 7 5.5 6.33 7 5.5 7z" />
                      </svg>
                      <span>{tag.name}</span>
                    </a>
                  ))}
                {otherMenu.map((menu, index) => (
                  <NavLink className="dropdown-item" to={menu.path} key={index}>
                    {mapIconOtherMenu(menu.title)}
                    <span>
                      {menu.title} ({getStatusCount(menu.status)})
                    </span>
                  </NavLink>
                ))}
              </Dropdown.Menu>
            </Dropdown>
          </div>
        </div>
      </div>
    </>
  );
}
const CustomToggle = React.forwardRef(({ children, onClick }, ref) => {
  return (
    <a
      href=""
      ref={ref}
      onClick={e => {
        e.preventDefault();
        onClick(e);
      }}
      className="text-center nm_item"
    >
      {children}
    </a>
  );
});
export default NavFilter;
