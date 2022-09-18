import '@/assets/js';
import DefaultLayout from '@Containers/Layout';
import PrivateRoute from '@Containers/PrivateRoute';
import { PageContractPublic } from '@Pages/PageContractPublic';
import { PageContractPrivate } from '@Pages/PageContractPrivate';
import { PageLoginCode } from '@Pages/PageLoginCode';
import { PageSignupCode } from '@Pages/PageSignupCode';
import { PageDashboard } from '@Pages/PageDashboard';
import { PagePolicy } from '@Pages/PagePolicy';
import { PageSearch } from '@Pages/PageSearch';
import { PageSecurity } from '@Pages/PageSecurity';
import { PageProfile } from '@Pages/PageProfile';
import { PageLogin } from '@Pages/PageLogin';
import { PageSignupProfile } from '@Pages/PageSignupProfile';
import { PageSignup } from '@Pages/PageSignup';
import { PageTest } from '@Pages/PageTest';
import { BrowserRouter, Route, Switch, useLocation } from 'react-router-dom';
import { PageContracts } from './Pages/PageContracts';
import { useDispatch, useSelector } from 'react-redux';
import AuthService from './Services/auth';
import { setLoadingProfile, setUser } from './Redux/Actions/Auth';
import { useEffect } from 'react';
import AlertModal from '@Components/AlertModal';
import InstallToolModal from '@Components/InstallToolModal';
import Toasts from '@Components/Toasts';

function App() {
  const isLoadingProfile = useSelector(state => state.auth.isLoadingProfile);
  const location = window.location;
  const isAuthenticated = AuthService.isAuthenticated();

  const dispatch = useDispatch();

  useEffect(() => {
    if (isAuthenticated && location.pathname.indexOf('ky-hop-dong') == -1) {
      AuthService.profile()
        .then(res => {
          dispatch(setUser(res.data));
          dispatch(setLoadingProfile(false));
        })
        .catch(err => {
          dispatch(setLoadingProfile(false));
        });
    } else {
      dispatch(setLoadingProfile(false));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  console.log('14/12/2021')
  return (
    <>
      {!isLoadingProfile ? (
        <>
          <BrowserRouter>
            <Switch>
              <Route exact path="/dang-nhap" component={PageLogin} />
              <Route exact path="/dang-ki" component={PageSignup} />
              <Route path="/dang-nhap/ma-xac-thuc" component={PageLoginCode} />
              <Route path="/dang-ki/ma-xac-thuc" component={PageSignupCode} />
              <Route exact path="/hop-dong/:slug" component={PageContractPrivate} />
              <Route path="/ky-hop-dong/:slug" component={PageContractPublic} />
              <Route path="/cap-nhat-thong-tin" component={PageSignupProfile} />
              <Route path="/test" component={PageTest} />
              <PrivateRoute>
                <DefaultLayout>
                  <Route exact path={['/trang-chu']} component={PageDashboard} />
                  <Route exact path="/thong-tin-ca-nhan" component={PageProfile} />
                  <Route exact path="/tra-cuu" component={PageSearch} />
                  <Route exact path="/tra-cuu/dieu-khoan" component={PagePolicy} />
                  <Route exact path="/tra-cuu/chinh-sach" component={PageSecurity} />
                  <Route path="/tat-ca-hop-dong" render={() => <PageContracts title="MContract" />} />
                  <Route path="/cho-xu-ly" render={() => <PageContracts title="MContract" status="PROCESSING" />} />
                  <Route path="/cho-doi-tac" render={() => <PageContracts title="MContract" status="WAITINGFORPARTNER" />} />
                  <Route path="/sai-xac-thuc" render={() => <PageContracts title="MContract" status="AUTHENTICATIONFAIL" />} />
                  <Route path="/hoan-thanh" render={() => <PageContracts title="MContract" status="COMPLETE" />} />
                  <Route path="/het-han" render={() => <PageContracts title="MContract" status="EXPIRED" />} />
                  <Route path="/huy-bo" render={() => <PageContracts title="MContract" status="CANCEL" />} />
                  <Route path="/tim-kiem-hop-dong" render={() => <PageContracts title="MContract" />} />
                  <Route path="/hop-dong-theo-the" render={() => <PageContracts title="MContract" />} />
                </DefaultLayout>
              </PrivateRoute>
            </Switch>
          </BrowserRouter>
        </>
      ) : (
        <>Loading dữ liệu người dùng</>
      )}
      <AlertModal />
      <InstallToolModal />
      <Toasts />
    </>
  );
}

export default App;
