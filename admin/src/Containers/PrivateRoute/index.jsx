import { useSelector } from 'react-redux';
import { Redirect, Route } from 'react-router';
import AuthService from '@Services/auth';
import { useLocation } from 'react-router-dom';
const publicRoutes = ['/cap-nhat-thong-tin'];
const validRoutes = [
  '/dang-nhap',
  '/dang-ki',
  '/dang-nhap/ma-xac-thuc',
  '/dang-ki/ma-xac-thuc',
  '/hop-dong',
  '/ky-hop-dong',
  '/cap-nhat-thong-tin',
  '/trang-chu',
  '/thong-tin-ca-nhan',
  '/tra-cuu',
  '/tra-cuu/dieu-khoan',
  '/tra-cuu/chinh-sach',
  '/tat-ca-hop-dong',
  '/cho-xu-ly',
  '/cho-doi-tac',
  '/sai-xac-thuc',
  '/hoan-thanh',
  '/het-han',
  '/huy-bo',
  '/tim-kiem-hop-dong',
  '/hop-dong-theo-the',
];
function PrivateRoute({ children, ...rest }) {
  const isAuthenticated = AuthService.isAuthenticated();
  const user = useSelector(state => state.auth.user);
  const location = useLocation();
  if (!isAuthenticated) {
    return <Redirect to="/dang-nhap" />;
  } else {
    if (!user || !user.fullName) {
      return <Redirect to="/cap-nhat-thong-tin" />;
    } else {
      if (publicRoutes.indexOf(rest.path) > -1) {
        return <Redirect to="/trang-chu" />;
      }
    }
  }
  if (location.pathname != '/' && validRoutes.indexOf(location.pathname) === -1) {
    return <Redirect to="/trang-chu" />
  }

  return <>{children}</>;
}
export default PrivateRoute;
