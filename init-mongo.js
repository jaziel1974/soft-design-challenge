db.createUser(
    {
        user: "mongoadmin",
        pwd: "Mongo2024",
        roles: [
            {
                role: "readWrite",
                db: "pollsystem"
            }
        ]
    }
);
db.createCollection("test");