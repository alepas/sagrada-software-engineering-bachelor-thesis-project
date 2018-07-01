package server.model.users;


import org.junit.jupiter.api.*;
import shared.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import shared.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import shared.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import shared.exceptions.usersAndDatabaseExceptions.CannotUpdateStatsForUserException;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class DatabaseUsersTest {
    static private DatabaseUsers db1, db2, db3, db4;
    static String path = "src/test/resources/databasetest/database.db";
    static File f;


    @BeforeAll
    static void setUpClass() {
        f = new File(path);
        if (f.isFile())
            f.delete();
        db1 = DatabaseUsers.getInstance(path);
        db2 = DatabaseUsers.getInstance(path);
        db3 = DatabaseUsers.getInstance(path);
        db4 = DatabaseUsers.getInstance(path);

    }

    @Test
    public void getInstanceTest() {
        Assertions.assertEquals(db1, db2);
        Assertions.assertEquals(db2, db3);
        Assertions.assertEquals(db3, db4);
    }

    @Test
    public void registerUserTest() {
        Assertions.assertDoesNotThrow(() -> {
            db1.registerUser("baffo", "12345");
        });
    }

    @Test
    public void registerTheSameUser() {
        Assertions.assertDoesNotThrow(() -> {
            db1.registerUser("affo", "12345");
        });
        Assertions.assertThrows(CannotRegisterUserException.class, () -> {
            db1.registerUser("affo", "12345");
        });
    }



    @Nested @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class neededDBStoredData {


        @Test
        public void loginRegisteredUser() {
            Assertions.assertDoesNotThrow(() -> {
                db1.login("affo", "12345");
            });
        }

        @Test
        void loginWrongPasswordUser() {
            Assertions.assertThrows(CannotLoginUserException.class, () -> {
                db1.login("affo", "wrongPassword");
            });

        }


        @Test
        void loginWrongUser() {
            Assertions.assertThrows(CannotLoginUserException.class, () -> {
                db1.login("bababa", "12345");
            });
        }


        @Test
        void differentTokensForDifferentUsers() throws CannotLoginUserException {
            Assertions.assertNotEquals(db1.login("affo", "12345"), db1.login("baffo", "12345"));
        }

        @Test
        void differentTokensSameUserMoreLogins() throws CannotLoginUserException {
            Assertions.assertNotEquals(db1.login("affo", "12345"), db1.login("affo", "12345"));
        }
//TODO test da rimuovere forse
        /*
        @Test
        void getWonGamesFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getWonGamesFromToken("WrongToken123");
            });
        }

        @Test
        void getWonGamesFromNullToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getWonGamesFromToken(null);
            });
        }


        @Test
        void getLostGamesFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getLostGamesFromToken("WrongToken123");
            });
        }

        @Test
        void getLostGamesFromNullToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getLostGamesFromToken(null);
            });
        }


        @Test
        void getAbandonedGamesFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getAbandonedGamesFromToken("WrongToken123");
            });
        }

        @Test
        void getAbandonedGamesFromNullToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getAbandonedGamesFromToken(null);
            });
        }


        @Test
        void getRankingFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getRankingFromToken("WrongToken123");
            });
        }

        @Test
        void getRankingFromNullToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getRankingFromToken(null);
            });
        }*/


        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class neededDbToken {

            String token = db1.registerUser("suse", "12345");

            neededDbToken() throws CannotRegisterUserException {
            }
//TODO test da rimuovere forse

/*
            @Test
            void getRankingFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getRankingFromToken(token);
                });
            }

            @Test
            void getWonGamesFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getWonGamesFromToken(token);
                });
            }

            @Test
            void getAbandonedGamesFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getAbandonedGamesFromToken(token);
                });
            }

            @Test
            void getLostGamesFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getLostGamesFromToken(token);
                });
            }*/
        }


        @Nested
        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class testFromUsername {



            testFromUsername() throws CannotRegisterUserException {

                db1.registerUser("ilario","test");
                db1.registerUser("suseppe", "prese");
            }


            @Test
            void getWonGamesFromUsernameTest() throws CannotFindUserInDBException {

                Assertions.assertEquals(0, db1.getWonGamesFromUsername("suseppe"));
            }

            @Test
            void addWonGamesFromUsernameTest() throws CannotFindUserInDBException, CannotUpdateStatsForUserException {
                int old=db1.getWonGamesFromUsername("ilario");
                db1.addWonGamesFromUsername("ilario");
                Assertions.assertEquals(old+1, db1.getWonGamesFromUsername("ilario"));
            }


            @Test
            void getLostGamesFromUsernameTest() throws CannotFindUserInDBException {
                Assertions.assertEquals(0, db1.getLostGamesFromUsername("suseppe"));
            }

            @Test
            void addLostGamesFromUsernameTest() throws CannotFindUserInDBException, CannotUpdateStatsForUserException {
                int old=db1.getLostGamesFromUsername("ilario");
                db1.addLostGamesFromUsername("ilario");
                Assertions.assertEquals(old+1, db1.getLostGamesFromUsername("ilario"));
            }


            @Test
            void getAbandonedGamesFromUsernameTest() throws CannotFindUserInDBException {
                Assertions.assertEquals(0, db1.getAbandonedGamesFromUsername("suseppe"));
            }

            @Test
            void addAbandonedGamesFromUsernameTest() throws CannotUpdateStatsForUserException, CannotFindUserInDBException {
                int old=db1.getAbandonedGamesFromUsername("ilario");
                db1.addAbandonedGamesFromUsername("ilario");
                Assertions.assertEquals(old+1, db1.getAbandonedGamesFromUsername("ilario"));
            }


            @Test
            void getRankingFromUsernameTest() throws CannotFindUserInDBException {
                Assertions.assertEquals(0, db1.getRankingFromUsername("suseppe"));
            }

            @Test
            void addPointsToRankingFromUsernameTest() throws CannotUpdateStatsForUserException, CannotFindUserInDBException {
                int old=db1.getRankingFromUsername("ilario");
                db1.addPointsRankingFromUsername("ilario", 12);
                Assertions.assertEquals(old+12, db1.getRankingFromUsername("ilario"));
            }


        }
    }






    @AfterAll
    static void removeFile() {
        f= new File(path);
        if (f != null) {
             f.delete();
        }
    }
}


